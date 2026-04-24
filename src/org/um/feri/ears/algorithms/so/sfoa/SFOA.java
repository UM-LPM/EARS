package org.um.feri.ears.algorithms.so.sfoa;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;

public class SFOA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> globalBest;

    private static final double GP = 0.5; // exploration probability

    public SFOA() {
        this(30);
    }

    public SFOA(int popSize) {
        this.popSize = popSize;

        au = new Author("Marko Milenovic", "marko.milenovic@student.um.si");
        ai = new AlgorithmInfo(
                "SFOA",
                "Starfish Optimization Algorithm",
                "@article{zhong2025starfish,"
                        + "title={Starfish Optimization Algorithm (SFOA)},"
                        + "author={Zhong, Changting et al.},"
                        + "journal={Neural Computing and Applications},"
                        + "year={2025}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task)
            throws StopCriterionException {

        this.task = task;
        initPopulation();

        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS)
            maxIt = task.getMaxIterations();
        else if (task.getStopCriterion() == StopCriterion.EVALUATIONS)
            maxIt = (task.getMaxEvaluations() - popSize) / popSize;

        while (!task.isStopCriterion()) {

            int T = task.getNumberOfIterations() + 1;
            double theta = Math.PI / 2.0 * T / maxIt;
            double tEO = (maxIt - T) / (double) maxIt * Math.cos(theta);

            ArrayList<NumberSolution<Double>> newPopulation = new ArrayList<>();

            if (RNG.nextDouble() < GP) {
                // ===== EXPLORATION =====
                for (int i = 0; i < popSize; i++) {
                    NumberSolution<Double> xi = population.get(i);
                    double[] newX = Util.toDoubleArray(xi.getVariables());

                    int nD = task.problem.getNumberOfDimensions();

                    if (nD > 5) {
                        int[] dims = RNG.randomPermutation(nD);
                        for (int k = 0; k < 5; k++) {
                            int j = dims[k];
                            double pm = (2 * RNG.nextDouble() - 1) * Math.PI;

                            if (RNG.nextDouble() < GP) {
                                newX[j] = xi.getValue(j)
                                        + pm * (globalBest.getValue(j) - xi.getValue(j)) * Math.cos(theta);
                            } else {
                                newX[j] = xi.getValue(j)
                                        - pm * (globalBest.getValue(j) - xi.getValue(j)) * Math.sin(theta);
                            }
                        }
                    } else {
                        int j = RNG.nextInt(nD);
                        int[] im = RNG.randomPermutation(popSize);

                        double r1 = 2 * RNG.nextDouble() - 1;
                        double r2 = 2 * RNG.nextDouble() - 1;

                        newX[j] = tEO * xi.getValue(j)
                                + r1 * (population.get(im[0]).getValue(j) - xi.getValue(j))
                                + r2 * (population.get(im[1]).getValue(j) - xi.getValue(j));
                    }

                    task.problem.makeFeasible(newX);
                    newPopulation.add(new NumberSolution<>(Util.toDoubleArrayList(newX)));
                }
            } else {
                // ===== EXPLOITATION =====
                int[] df = RNG.randomPermutation(popSize);
                double[][] dm = new double[5][];

                for (int k = 0; k < 5; k++) {
                    dm[k] = new double[task.problem.getNumberOfDimensions()];
                    for (int d = 0; d < dm[k].length; d++) {
                        dm[k][d] = globalBest.getValue(d) - population.get(df[k]).getValue(d);
                    }
                }

                for (int i = 0; i < popSize; i++) {
                    NumberSolution<Double> xi = population.get(i);
                    double[] newX = Util.toDoubleArray(xi.getVariables());

                    int[] kp = RNG.randomPermutation(5);
                    double r1 = RNG.nextDouble();
                    double r2 = RNG.nextDouble();

                    for (int d = 0; d < newX.length; d++) {
                        newX[d] = xi.getValue(d)
                                + r1 * dm[kp[0]][d]
                                + r2 * dm[kp[1]][d];
                    }

                    if (i == popSize - 1) {
                        double factor = Math.exp(-(double) T * popSize / maxIt);
                        for (int d = 0; d < newX.length; d++) {
                            newX[d] = factor * xi.getValue(d);
                        }
                    }

                    task.problem.makeFeasible(newX);
                    newPopulation.add(new NumberSolution<>(Util.toDoubleArrayList(newX)));
                }
            }

            for (int i = 0; i < popSize; i++) {
                if (task.isStopCriterion())
                    break;

                NumberSolution<Double> candidate = newPopulation.get(i);
                task.eval(candidate);

                if (task.problem.isFirstBetter(candidate, population.get(i))) {
                    population.set(i, candidate);

                    if (task.problem.isFirstBetter(candidate, globalBest)) {
                        globalBest = candidate;
                    }
                }
            }
            task.incrementNumberOfIterations();
        }
        return globalBest;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.add(task.generateRandomEvaluatedSolution());
        }

        population.sort(new ProblemComparator<>(task.problem));
        globalBest = population.get(0);
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
