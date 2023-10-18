package org.um.feri.ears.algorithms.moo.nsga3;


import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentalSelection<N extends Number> {

    private List<List<NumberSolution<N>>> fronts;
    private int solutionsToSelect;
    private List<ReferencePoint<N>> referencePoints;
    private int numberOfObjectives;


    public EnvironmentalSelection(List<List<NumberSolution<N>>> fronts, int solutionsToSelect, List<ReferencePoint<N>> referencePoints, int numberOfObjectives) {
        this.fronts = fronts;
        this.solutionsToSelect = solutionsToSelect;
        this.referencePoints = referencePoints;
        this.numberOfObjectives = numberOfObjectives;
    }

    public List<Double> translateObjectives(List<NumberSolution<N>> population) {
        List<Double> idealPoint;

        idealPoint = new ArrayList<>(numberOfObjectives);

        for (int f = 0; f < numberOfObjectives; f += 1) {
            double minf = Double.MAX_VALUE;
            for (int i = 0; i < fronts.get(0).size(); i += 1) // min values must appear in the first front
            {
                minf = Math.min(minf, fronts.get(0).get(i).getObjective(f));
            }
            idealPoint.add(minf);

            for (List<NumberSolution<N>> list : fronts) {
                for (NumberSolution<N> s : list) {
                    if (f == 0) // in the first objective we create the vector of convObjs
                        setAttribute(s, new ArrayList<Double>());

                    getAttribute(s).add(s.getObjective(f) - minf);

                }
            }
        }

        return idealPoint;
    }


    // ----------------------------------------------------------------------
    // ASF: Achivement Scalarization Function
    // I implement here a effcient version of it, which only receives the index
    // of the objective which uses 1.0; the rest will use 0.00001. This is
    // different to the one impelemented in C++
    // ----------------------------------------------------------------------
    private double ASF(NumberSolution<N> s, int index) {
        double maxRatio = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < s.getNumberOfObjectives(); i++) {
            double weight = (index == i) ? 1.0 : 0.000001;
            maxRatio = Math.max(maxRatio, s.getObjective(i) / weight);
        }
        return maxRatio;
    }

    // ----------------------------------------------------------------------
    private List<NumberSolution<N>> findExtremePoints(List<NumberSolution<N>> population) {
        List<NumberSolution<N>> extremePoints = new ArrayList<>();
        NumberSolution<N> minIndv = null;
        for (int f = 0; f < numberOfObjectives; f += 1) {
            double minASF = Double.MAX_VALUE;
            for (NumberSolution<N> s : fronts.get(0)) { // only consider the individuals in the first front
                double asf = ASF(s, f);
                if (asf < minASF) {
                    minASF = asf;
                    minIndv = s;
                }
            }

            extremePoints.add(minIndv);
        }
        return extremePoints;
    }

    public List<Double> guassianElimination(List<List<Double>> A, List<Double> b) {
        List<Double> x = new ArrayList<>();

        int N = A.size();
        for (int i = 0; i < N; i += 1) {
            A.get(i).add(b.get(i));
        }

        for (int base = 0; base < N - 1; base += 1) {
            for (int target = base + 1; target < N; target += 1) {
                double ratio = A.get(target).get(base) / A.get(base).get(base);
                for (int term = 0; term < A.get(base).size(); term += 1) {
                    A.get(target).set(term, A.get(target).get(term) - A.get(base).get(term) * ratio);
                }
            }
        }

        for (int i = 0; i < N; i++)
            x.add(0.0);

        for (int i = N - 1; i >= 0; i -= 1) {
            for (int known = i + 1; known < N; known += 1) {
                A.get(i).set(N, A.get(i).get(N) - A.get(i).get(known) * x.get(known));
            }
            x.set(i, A.get(i).get(N) / A.get(i).get(i));
        }
        return x;
    }

    public List<Double> constructHyperplane(List<NumberSolution<N>> population, List<NumberSolution<N>> extremePoints) {
        // Check whether there are duplicate extreme points.
        // This might happen but the original paper does not mention how to deal with it.
        boolean duplicate = false;
        for (int i = 0; !duplicate && i < extremePoints.size(); i += 1) {
            for (int j = i + 1; !duplicate && j < extremePoints.size(); j += 1) {
                duplicate = extremePoints.get(i).equals(extremePoints.get(j));
            }
        }

        List<Double> intercepts = new ArrayList<>();

        if (duplicate) // cannot construct the unique hyperplane (this is a casual method to deal with the condition)
        {
            for (int f = 0; f < numberOfObjectives; f += 1) {
                // extremePoints[f] stands for the individual with the largest value of objective f
                intercepts.add(extremePoints.get(f).getObjective(f));
            }
        } else {
            // Find the equation of the hyperplane
            List<Double> b = new ArrayList<>(); //(pop[0].objs().size(), 1.0);
            for (int i = 0; i < numberOfObjectives; i++)
                b.add(1.0);

            List<List<Double>> A = new ArrayList<>();
            for (NumberSolution<N> s : extremePoints) {
                List<Double> aux = new ArrayList<>();
                for (int i = 0; i < numberOfObjectives; i++)
                    aux.add(s.getObjective(i));
                A.add(aux);
            }
            List<Double> x = guassianElimination(A, b);

            // Find intercepts
            for (int f = 0; f < numberOfObjectives; f += 1) {
                intercepts.add(1.0 / x.get(f));

            }
        }
        return intercepts;
    }

    public void normalizeObjectives(List<NumberSolution<N>> population, List<Double> intercepts, List<Double> idealPoint) {
        for (int t = 0; t < fronts.size(); t += 1) {
            for (NumberSolution<N> s : fronts.get(t)) {

                for (int f = 0; f < numberOfObjectives; f++) {
                    List<Double> convObj = (List<Double>) getAttribute(s);
                    if (Math.abs(intercepts.get(f) - idealPoint.get(f)) > 10e-10) {
                        convObj.set(f, convObj.get(f) / (intercepts.get(f) - idealPoint.get(f)));
                    } else {
                        convObj.set(f, convObj.get(f) / (10e-10));
                    }

                }
            }
        }
    }

    public double perpendicularDistance(List<Double> direction, List<Double> point) {
        double numerator = 0, denominator = 0;
        for (int i = 0; i < direction.size(); i += 1) {
            numerator += direction.get(i) * point.get(i);
            denominator += Math.pow(direction.get(i), 2.0);
        }
        double k = numerator / denominator;

        double d = 0;
        for (int i = 0; i < direction.size(); i += 1) {
            d += Math.pow(k * direction.get(i) - point.get(i), 2.0);
        }
        return Math.sqrt(d);
    }


    public void associate(List<NumberSolution<N>> population) {

        double d;
        for (int t = 0; t < fronts.size(); t++) {
            for (NumberSolution<N> s : fronts.get(t)) {
                int minRp = -1;
                double minDist = Double.MAX_VALUE;
                for (int r = 0; r < this.referencePoints.size(); r++) {
                    d = perpendicularDistance(this.referencePoints.get(r).position,
                            (List<Double>) getAttribute(s));

                    if (d < minDist) {
                        minDist = d;
                        minRp = r;
                    }
                }
                if (t + 1 != fronts.size()) {
                    this.referencePoints.get(minRp).AddMember();
                } else {
                    this.referencePoints.get(minRp).AddPotentialMember(s, minDist);
                }
            }
        }

    }

    int FindNicheReferencePoint() {
        // find the minimal cluster size
        int minSize = Integer.MAX_VALUE;
        for (ReferencePoint referencePoint : this.referencePoints)
            minSize = Math.min(minSize, referencePoint.MemberSize());

        // find the reference points with the minimal cluster size Jmin
        List<Integer> minRps = new ArrayList<>();


        for (int r = 0; r < this.referencePoints.size(); r += 1) {
            if (this.referencePoints.get(r).MemberSize() == minSize) {
                minRps.add(r);
            }
        }
        // return a random reference point (j-bar)
        return minRps.get(minRps.size() > 1 ? RNG.nextInt(minRps.size()) : 0);
    }

    // ----------------------------------------------------------------------
    // SelectClusterMember():
    //
    // Select a potential member (an individual in the front Fl) and associate
    // it with the reference point.
    //
    // Check the last two paragraphs in Section IV-E in the original paper.
    // ----------------------------------------------------------------------
    NumberSolution<N> SelectClusterMember(ReferencePoint rp) {
        NumberSolution<N> chosen = null;
        if (rp.HasPotentialMember()) {
            if (rp.MemberSize() == 0) // currently has no member
            {
                chosen = rp.FindClosestMember();
            } else {
                chosen = rp.RandomMember();
            }
        }
        return chosen;
    }


    /* This method performs the environmental Selection indicated in the paper describing NSGAIII*/
    public List<NumberSolution<N>> execute(List<NumberSolution<N>> source) {
        // The comments show the C++ code

        // ---------- Steps 9-10 in Algorithm 1 ----------
        if (source.size() == this.solutionsToSelect) return source;


        // ---------- Step 14 / Algorithm 2 ----------
        //vector<double> idealPoint = TranslateObjectives(&cur, fronts);
        List<Double> idealPoint = translateObjectives(source);
        List<NumberSolution<N>> extremePoints = findExtremePoints(source);
        List<Double> intercepts = constructHyperplane(source, extremePoints);

        normalizeObjectives(source, intercepts, idealPoint);
        // ---------- Step 15 / Algorithm 3, Step 16 ----------
        associate(source);

        // ---------- Step 17 / Algorithm 4 ----------
        while (source.size() < this.solutionsToSelect) {
            int minRp = FindNicheReferencePoint();

            NumberSolution<N> chosen = SelectClusterMember(this.referencePoints.get(minRp));
            if (chosen == null) // no potential member in Fl, disregard this reference point
            {
                this.referencePoints.remove(minRp);
            } else {
                this.referencePoints.get(minRp).AddMember();
                this.referencePoints.get(minRp).RemovePotentialMember(chosen);
                source.add(chosen);
            }
        }

        return source;
    }

    public void setAttribute(NumberSolution<N> solution, List<Double> value) {
        solution.setAttribute(getAttributeID(), value);
    }


    public List<Double> getAttribute(NumberSolution<N> solution) {
        return (List<Double>) solution.getAttribute(getAttributeID());
    }


    public Object getAttributeID() {
        return this.getClass();
    }

}
