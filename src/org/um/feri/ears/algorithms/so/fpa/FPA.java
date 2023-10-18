/*
 * Flower Pollination Algorithm by Xin-She Yang in Java.
 */
package org.um.feri.ears.algorithms.so.fpa;

import org.apache.commons.math3.special.Gamma;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;


import java.util.ArrayList;

public class FPA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter
    private double lambda;
    @AlgorithmParameter(name = "switch probability")
    private double switchProbability;
    // ND: Normal distribution
    private static final double meanND = 0.0;
    private static final double stdDevND = 1.0;

    private NumberSolution<Double> best;
    private ArrayList<NumberSolution<Double>> population;

    public FPA() {
        this(20, 1.5, 0.8);
    }

    public FPA(int popSize, double lambda, double switchProbability) {
        super();
        this.popSize = popSize;
        this.lambda = lambda;
        this.switchProbability = switchProbability;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("FPA", "Flower Pollination Algorithm",
                "@inproceedings{yang2012flower,"
                        + "title={Flower pollination algorithm for global optimization},"
                        + "author={Yang, Xin-She},"
                        + "booktitle={International Conference on Unconventional Computing and Natural Computation},"
                        + "pages={240--249},"
                        + "year={2012},"
                        + "organization={Springer}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        double[] candidate;
        double[] levy;

        int rand1, rand2;
        double epsilon;
        while (!task.isStopCriterion()) {

            for (int i = 0; i < popSize; i++) {

                candidate = new double[task.problem.getNumberOfDimensions()];
                if (RNG.nextDouble() > switchProbability) {
                    /* Global Pollination */
                    levy = levy();

                    for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                        candidate[j] = population.get(i).getValue(j) + levy[j] * (best.getValue(j) - population.get(i).getValue(j));
                    }
                } else {
                    /* Local Pollination */
                    epsilon = RNG.nextDouble();

                    do {
                        rand1 = RNG.nextInt(popSize);
                    } while (rand1 == i);
                    do {
                        rand2 = RNG.nextInt(popSize);
                    } while (rand2 == rand1);

                    for (int j = 0; j < task.problem.getNumberOfDimensions(); j++)
                        candidate[j] += epsilon * (population.get(rand1).getValue(j) - population.get(rand2).getValue(j));
                }

                // Check bounds
                task.problem.setFeasible(candidate);

                // Evaluate new solution
                if (task.isStopCriterion())
                    break;


                NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(candidate));
                task.eval(newSolution);
                // If the new solution is better: Replace
                if (task.problem.isFirstBetter(newSolution, population.get(i))) {
                    population.set(i, newSolution);
                }

                // Update best solution
                if (task.problem.isFirstBetter(newSolution, best)) {
                    best = new NumberSolution<>(newSolution);
                }

            }

            task.incrementNumberOfIterations();
        }

        return best;
    }

    /**
     * creates Levy flight samples
     */
    private double[] levy() {
        double[] step = new double[task.problem.getNumberOfDimensions()];

        double sigma = Math.pow(Gamma.gamma(1 + lambda) * Math.sin(Math.PI * lambda / 2)
                / (Gamma.gamma((1 + lambda) / 2) * lambda * Math.pow(2, (lambda - 1) / 2)), (1 / lambda));

        for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {

            double u = Distribution.normal(meanND, stdDevND) * sigma;
            double v = Distribution.normal(meanND, stdDevND);

            step[i] = 0.01 * u / (Math.pow(Math.abs(v), (1 - lambda)));
        }
        return step;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            NumberSolution<Double> newSolution = task.getRandomEvaluatedSolution();
            population.add(newSolution);
        }

        population.sort(new ProblemComparator<>(task.problem));
        best = new NumberSolution<>(population.get(0));
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
