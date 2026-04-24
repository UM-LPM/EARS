package org.um.feri.ears.algorithms.so.eao;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Locale;

public class EAO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    @AlgorithmParameter(name = "EC (lower bound for scale factors)")
    private double ec;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> best;

    public EAO() {
        this(30, 0.1);
    }

    public EAO(int popSize, double ec) {
        super();
        this.popSize = popSize;
        this.ec = ec;

        au = new Author("Kristina Čović", "kristina.covic@student.um.si");
        ai = new AlgorithmInfo(
                "EAO",
                "Enzyme Action Optimizer",
                "@article{rodan2025eao,"
                        + "  title={Enzyme action optimizer: a novel bio-inspired optimization algorithm},"
                        + "  author={Ali Rodan, Abdel-Karimi Al-Tamimi, Loai Al-Alnemer, Seyedali Mirjalili, Peter Tino},"
                        + "  journal={The Journal of Supercomputing},"
                        + "  year={2025},"
                        + "  doi={10.1007/s11227-025-07052-w}"
                        + "}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        final int dim = task.problem.getNumberOfDimensions();
        initPopulation();

        int maxIt = 10000;

        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        } else if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            // First + CandA + CandB => popSize * 3 evals/iter, plus init popSize.
            int remaining = Math.max(0, task.getMaxEvaluations() - popSize);
            int approx = remaining / Math.max(1, popSize * 3);
            maxIt = Math.max(1, approx);
        }

        while (!task.isStopCriterion()) {

            int it = task.getNumberOfIterations() + 1;
            // EQ 3
            double AF = Math.sqrt((double) it / (double) maxIt);
            if (AF > 1.0) AF = 1.0;

            for (int i = 0; i < popSize; i++) {
                if (task.isStopCriterion()) break;

                NumberSolution<Double> xiSol = population.get(i);
                double[] xi = toArray(xiSol);
                double[] xBest = toArray(best);

                // 1) FirstSubstratePosition = (Best - Xi) + rand_vec * sin(AF * Xi)
                // EQ 4
                double[] firstPos = new double[dim];
                for (int d = 0; d < dim; d++) {
                    double r = RNG.nextDouble();
                    firstPos[d] = (xBest[d] - xi[d]) + r * Math.sin(AF * xi[d]);
                }

                if (task.isStopCriterion())
                    break;

                NumberSolution<Double> firstSol = evaluateVector(firstPos);

                // 2) Pick 2 random distinct indices without replacement,
                // EQ 5
                int[] pair = drawPairNotIncludingI(i, popSize);
                int a = pair[0];
                int b = pair[1];

                double[] s1 = toArray(population.get(a));
                double[] s2 = toArray(population.get(b));

                // 3) CandidateA (vector random factors) -- IMPORTANT:
                double[] scA1v = new double[dim];
                for (int d = 0; d < dim; d++) {
                    scA1v[d] = ec + (1.0 - ec) * RNG.nextDouble();
                }

                double[] exAv = new double[dim];
                for (int d = 0; d < dim; d++) {
                    exAv[d] = (ec + (1.0 - ec) * RNG.nextDouble()) * AF;
                }

                double[] candA = new double[dim];
                for (int d = 0; d < dim; d++) {
                    candA[d] = xi[d]
                            + scA1v[d] * (s1[d] - s2[d])
                            + exAv[d] * (xBest[d] - xi[d]);
                }

                if (task.isStopCriterion())
                    break;

                NumberSolution<Double> solA = evaluateVector(candA);

                // 4) CandidateB: scalar scB1, scalar exB
                // EQ 6
                double scB1 = ec + (1.0 - ec) * RNG.nextDouble();
                double exB = (ec + (1.0 - ec) * RNG.nextDouble()) * AF;

                double[] candB = new double[dim];
                for (int d = 0; d < dim; d++) {
                    candB[d] = xi[d]
                            + scB1 * (s1[d] - s2[d])
                            + exB * (xBest[d] - xi[d]);
                }

                if (task.isStopCriterion())
                    break;

                NumberSolution<Double> solB = evaluateVector(candB);

                // 5) Second = better(A,B)
                // EQ7 and EQ8
                NumberSolution<Double> secondSol = task.problem.isFirstBetter(solA, solB) ? solA : solB;

                // 6) Updated = better(First, Second)
                NumberSolution<Double> upd = task.problem.isFirstBetter(firstSol, secondSol) ? firstSol : secondSol;

                // 7) Replace Xi only if improved (Python: UpdatedFitness < ReactionRate[i])
                boolean replaced = false;
                if (task.problem.isFirstBetter(upd, xiSol)) {
                    population.set(i, upd);
                    xiSol = upd;
                    replaced = true;
                }

                // 8) Update global best only after a replacement (mirrors Python update block)
                // EQ 9
                if (replaced && task.problem.isFirstBetter(xiSol, best)) {
                    best = xiSol;
                }
            }
            task.incrementNumberOfIterations();
        }
        return best;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>(popSize);
        best = null;

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) break;

            NumberSolution<Double> s = task.generateRandomEvaluatedSolution();
            population.add(s);

            if (best == null || task.problem.isFirstBetter(s, best)) {
                best = s;
            }
        }
    }

    private NumberSolution<Double> evaluateVector(double[] x) throws StopCriterionException {
        task.problem.makeFeasible(x);
        NumberSolution<Double> s = new NumberSolution<>(Util.toDoubleArrayList(x));
        task.eval(s);
        return s;
    }

    private static double[] toArray(NumberSolution<Double> s) {
        double[] x = new double[s.numberOfVariables()];
        for (int d = 0; d < s.numberOfVariables(); d++) {
            x[d] = s.getValue(d);
        }
        return x;
    }

    /**
     * EXACT pair draw used in your Python tape parity runner:
     * - draw a = nextInt(n)
     * - draw b = nextInt(n)
     * - while b==a: draw b again
     * - if i is included, repeat the WHOLE procedure (draw new a and b)
     */
    private static int[] drawPairNotIncludingI(int i, int n) {
        while (true) {
            int a = RNG.nextInt(n);
            int b = RNG.nextInt(n);
            while (b == a) {
                b = RNG.nextInt(n);
            }
            if (a != i && b != i) {
                return new int[]{a, b};
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        population = null;
        best = null;
    }
}
