package org.um.feri.ears.algorithms.so.bpbo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.*;

public class BPBO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    @AlgorithmParameter(name = "Pi")
    private double Pi;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> prey; // The best solution found so far


    public BPBO() {
        this(30); // Default population size and debug off
    }

    public BPBO(int popSize) {
        super();
        this.popSize = popSize;
        this.Pi = 0.7;

        au = new Author("Lucijan Hrastnik", "");

        ai = new AlgorithmInfo("BPBO", "Birds of Prey-Based Optimization",
                "@article{ghasemi2025bpbo,"
                        + "  title={Birds of prey-based optimization (BPBO): a metaheuristic algorithm for optimization},"
                        + "  author={Ghasemi, M., Akbari, M.A., Zare, M. et al.},"
                        + "  journal={Evol. Intel.},"
                        + "  volume={18},"
                        + "  pages={88},"
                        + "  year={2025},"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        while (!task.isStopCriterion()) {

            double[] meanPosition = new double[task.problem.getNumberOfDimensions()];
            for (int d = 0; d < task.problem.getNumberOfDimensions(); d++) {
                double sum = 0;
                for (int i = 0; i < popSize; i++) {
                    sum += population.get(i).getValue(d);
                }
                meanPosition[d] = sum / popSize;
            }

            for (int i = 1; i < popSize; i++) {
                if (population.get(i).getObjectives()[0] < prey.getObjectives()[0]) {
                    prey = population.get(i);
                }
            }

            for (int i = 0; i < popSize; i++) {
                if (task.isStopCriterion()) break;
                NumberSolution<Double> currentBird = population.get(i);
                double[] newPosition = new double[task.problem.getNumberOfDimensions()];
                NumberSolution<Double> lastBird = population.get(popSize - 1);

                double a = RNG.nextDouble();
                if (a < Pi) {
                    double b = RNG.nextDouble();
                    double c = RNG.nextDouble();
                    if (b < c) {
                        double e = RNG.nextDouble();
                        int M00 = (int) Math.round(1 + e);
                        for (int d = 0; d < task.problem.getNumberOfDimensions(); d++) {
                            double f = RNG.nextDouble();
                            newPosition[d] = currentBird.getValue(d) + f * (prey.getValue(d) - M00 * currentBird.getValue(d));
                        }
                    } else if (RNG.nextDouble() < 0.5) { // replaced RNG.nextDouble() < RNG.nextDouble() for redability
                        double j = RNG.nextDouble();
                        int M01 = (int) Math.round(1 + j);
                        for (int d = 0; d < task.problem.getNumberOfDimensions(); d++) {
                            double k = RNG.nextDouble();
                            newPosition[d] = meanPosition[d] + k * (prey.getValue(d) - M01 * meanPosition[d]);
                        }
                    } else {
                        double l = RNG.nextDouble();
                        int M02 = (int) Math.round(1 + l);
                        for (int d = 0; d < task.problem.getNumberOfDimensions(); d++) {
                            double m = RNG.nextDouble();
                            newPosition[d] = currentBird.getValue(d) + m * (currentBird.getValue(d) - M02 * lastBird.getValue(d));
                        }
                    }
                } else {
                    double x = RNG.nextDouble();
                    for (int d = 0; d < task.problem.getNumberOfDimensions(); d++) {
                        newPosition[d] = currentBird.getValue(d) + x * RNG.nextDouble();
                    }
                }

                task.problem.makeFeasible(newPosition);
                NumberSolution<Double> newSol = new NumberSolution<>(Util.toDoubleArrayList(newPosition));
                task.eval(newSol);

                if (task.problem.isFirstBetter(newSol, currentBird)) {
                    population.set(i, newSol);
                    if (task.problem.isFirstBetter(newSol, prey)) {
                        prey = newSol;
                    }
                }
            }
            population.sort(new ProblemComparator<>(task.problem));
            task.incrementNumberOfIterations();
        }

        return prey;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        prey = task.generateRandomEvaluatedSolution();
        population.add(prey);
        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            NumberSolution<Double> newSol = task.generateRandomEvaluatedSolution();
            population.add(newSol);
            if (task.problem.isFirstBetter(newSol, prey)) {
                prey = newSol;
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}