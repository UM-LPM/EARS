package org.um.feri.ears.algorithms.so.gndo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.BoundaryControl;
import org.um.feri.ears.util.Point;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class GNDO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> population;

    public GNDO() {
        this(30);
    }

    public GNDO(int popSize) {
        super();
        this.popSize = popSize;
        
        au = new Author("mitko", "mitko.nikov@student.um.si");
        ai = new AlgorithmInfo("GNDO", "Generalized Normal Distribution Optimization",
            "@article{Zhang2020Nov,"
                    + "author = {Zhang, Yiying and Jin, Zhigang and Mirjalili, Seyedali},"
                    + "title = {{Generalized normal distribution optimization and its applications in parameter extraction of photovoltaic models}},"
                    + "journal = {Energy Convers. Manage.},"
                    + "volume = {224},"
                    + "pages = {113301},"
                    + "year = {2020},"
                    + "month = nov,"
                    + "issn = {0196-8904},"
                    + "publisher = {Pergamon},"
                    + "doi = {10.1016/j.enconman.2020.113301}"
                    + "}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        double[] fitness = new double[popSize];
        NumberSolution<Double> bestSolution = population.get(0);

        int iter = 1;
        while (!task.isStopCriterion()) {
            // Evaluate all solutions
            for (int index = 0; index < popSize; index++) {
                NumberSolution<Double> current = population.get(index);
                if (task.isStopCriterion()) break;
                task.eval(current);
                fitness[index] = current.getEval();
                if (task.problem.isFirstBetter(current, bestSolution)) {
                    bestSolution = current;
                }
            }

            double bestFitness = bestSolution.getEval();

            // Compute the mean of the whole population
            Point<Double> mean = new Point<Double>(population.get(0).getVariables().size());
            for (int index = 0; index < popSize; index++) {
                mean.add(population.get(index).getVariablesAsPoint());
            }
            mean.divide(popSize);
            
            for (int index = 0; index < popSize; index++) {
                Point<Double> newSolution = null;

                // Pick 3 random solutions
                // This is cleaner than the original source code
                int a, b, c;
                do {
                    a = RNG.nextInt(1, popSize) - 1;
                    b = RNG.nextInt(1, popSize) - 1;
                    c = RNG.nextInt(1, popSize) - 1;
                } while (a == index || a == b || c == b || c == a || c == index || b == index);
                
                // Get current positions as points
                Point<Double> ap = population.get(a).getVariablesAsPoint();
                Point<Double> bp = population.get(b).getVariablesAsPoint();
                Point<Double> cp = population.get(c).getVariablesAsPoint();
                Point<Double> ip = population.get(index).getVariablesAsPoint();

                // Compute movement vectors
                Point<Double> v1, v2;
                if (task.problem.isFirstBetter(population.get(a), population.get(index))) {
                    v1 = ap.subtractCopy(ip);
                } else {
                    v1 = ip.subtractCopy(ap);
                }

                if (task.problem.isFirstBetter(population.get(b), population.get(c))) {
                    v2 = bp.subtractCopy(cp);
                } else {
                    v2 = cp.subtractCopy(bp);
                }
                
                // You are probably wondering why the f is this here,
                // instead of just RNG.nextDouble() <= 0.5.
                // Well my friend, believe it or not, this was in the original source code.
                // And since we are recreating the original source code,
                // and matching the RNG generators, I have to write it like this,
                // so we don't need to modify the original source code.
                // What can I say... *disappointment*
                double r1 = RNG.nextDouble();
                double r2 = RNG.nextDouble();
                if (r1 <= r2) {
                    // Local Exploitation Strategy

                    // Eq 19
                    Point<Double> u = population.get(index).getVariablesAsPoint();
                    u.add(bestSolution.getVariablesAsPoint()).add(mean).divide(3);

                    // Eq 20
                    Point<Double> deta = population.get(index).getVariablesAsPoint();
                    deta.subtract(u).power(2); // part 1
                    deta.add(bestSolution.getVariablesAsPoint().subtract(u).power(2)); // part 2
                    deta.add(mean.copy().subtract(u).power(2)); // part 3
                    deta.divide(3).sqrt(); // avg and sqrt

                    // Generate VC1 and VC2
                    Point<Double> vc1 = generateRandomPoint(task.problem.getNumberOfDimensions());
                    Point<Double> vc2 = generateRandomPoint(task.problem.getNumberOfDimensions());

                    // Eq 21 (essentially similar to Box-Muller transform)
                    vc2.log().multiply(-1).sqrt();

                    Point<Double> vc1_1 = vc1.copy().multiply(2 * Math.PI).cos();
                    Point<Double> vc1_2 = vc1.copy().multiply(2 * Math.PI).add(Math.PI).cos();

                    Point<Double> z1 = vc2.copy().multiply(vc1_1);
                    Point<Double> z2 = vc2.copy().multiply(vc1_2);
                    
                    // Again... Don't ask why...
                    // Here, there's no need to copy u and deta,
                    // since we are not using them further
                    if (RNG.nextDouble() <= RNG.nextDouble()) {
                        newSolution = u.add(deta.multiply(z1));
                    } else {
                        newSolution = u.add(deta.multiply(z2));
                    }
                } else {
                    // Global Exploration Strategy
                    double beta = RNG.nextDouble();
                    double g1 = RNG.nextGaussian();
                    double g2 = RNG.nextGaussian();

                    Point<Double> v = population.get(index).getVariablesAsPoint();
                    
                    // Eq 23
                    // Here we take beta of the v1 and 1-beta of v2
                    Point<Double> t1 = v1.copy().multiply(beta * Math.abs(g1));
                    Point<Double> t2 = v2.copy().multiply((1 - beta) * Math.abs(g2));

                    newSolution = v.add(t1).add(t2);
                }

                // Clamp the new solution in the boundaries of the problem
                BoundaryControl.clamp(newSolution, task.problem.getLowerLimit(), task.problem.getUpperLimit());

                NumberSolution<Double> sol = new NumberSolution<Double>(newSolution);
                if (task.isStopCriterion()) break;
                task.eval(sol);
                if (task.problem.isFirstBetter(sol, population.get(index))) {
                    population.set(index, sol);
                    if (task.problem.isFirstBetter(sol, bestSolution)) {
                        bestSolution = sol;
                        bestFitness = sol.getEval();
                    }
                }
            }

            // To compare the iteration cost with the MATLAB version
            System.out.println("Iteration " + iter + ": Best Cost = " + bestFitness);
            iter++;
            
            task.incrementNumberOfIterations();
        }

        return bestSolution;
    }

    Point<Double> generateRandomPoint(int dims) {
        Point<Double> point = new Point<Double>(dims);
        for (int i = 0; i < dims; i++) {
            point.set(i, RNG.nextDouble());
        }
        return point;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<NumberSolution<Double>>();
        Point<Double> lb = new Point<Double>(task.problem.getLowerLimit());
        Point<Double> ub = new Point<Double>(task.problem.getUpperLimit());
        for (int i = 0; i < popSize; i++) {
            Point<Double> r = generateRandomPoint(task.problem.getNumberOfDimensions());
            
            // Make sure r is in bounds
            Point<Double> p = lb.copy().add(ub.copy().subtract(lb).multiply(r));
            population.add(new NumberSolution<Double>(p));
        }
    }

    @SuppressWarnings("unused")
    private static void printPoint(Point<Double> p) {
        for (int k = 0; k < p.getValues().length; k++) {
            System.out.print(p.getValues()[k] + " ");
        }
        System.out.println();
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
