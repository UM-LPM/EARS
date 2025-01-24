package org.um.feri.ears.algorithms.so.rsa;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.PredefinedRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class RSA extends NumberAlgorithm {
    @AlgorithmParameter(name = "population size")
    private int popSize;
    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> bestSolution;
    private double Alpha;  // Exploration control parameter
    private double Beta;   // Hunting cooperation control parameter
    public double UB;
    public double LB;
    public int dim;

    public RSA() {
        this(5);
    }
    public RSA(int popSize) {
        this(popSize, 0.1, 0.1);
    }

    public RSA(int popSize, double alpha, double beta) {
        super();
        this.popSize = popSize;
        this.Alpha = alpha;
        this.Beta = beta;

        au = new Author("Ivana", "ivana.avramoska@student.um.si");
        ai = new AlgorithmInfo("RSA", "Reptile Search Algorithm",
                "@article{sasmal2023rsa," +
                        "  title={Reptile Search Algorithm}," +
                        "  author={Buddhadev Sasmal, Abdelazim G. Hussien, Arunita Das, Krishna Gopal Dhal, Ramesh Saha}," +
                        "  journal={Advanced Algorithms Journal}," +
                        "  volume={2023}," +
                        "  pages={45--60}," +
                        "  year={2023}," +
                        "  publisher={Elsevier}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        this.UB = task.problem.upperLimit.get(0);
        this.LB = task.problem.lowerLimit.get(0);

        int maxIterations = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIterations = task.getMaxIterations();
        }

        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIterations = (task.getMaxEvaluations() - popSize) / popSize;
        }

        dim = task.problem.getNumberOfDimensions();

        double[][] newPosition = new double[popSize][dim];

        initPopulation();

        while (!task.isStopCriterion()) {

            double ES = 2 * (RNG.nextInt(2)) * (1 - ((double) task.getNumberOfIterations() / maxIterations)); // Probability Ratio

            for (int i = 1; i < popSize; i++) {
                NumberSolution<Double> reptile = population.get(i);

                for (int j = 0; j < dim; j++) {

                    double R = bestSolution.getValue(j)
                            - population.get(RNG.nextInt(popSize)).getValue(j)
                            / (bestSolution.getValue(j) + Double.MIN_VALUE);

                    double P = Alpha + (reptile.getValue(j) - mean(population.get(i)))
                            / (bestSolution.getValue(j) * (UB - LB) + Double.MIN_VALUE);

                    double Eta = bestSolution.getValue(j) * P;

                    if (task.getNumberOfIterations() < maxIterations / 4) {
                        newPosition[i][j] = bestSolution.getValue(j) - Eta * Beta - R * RNG.nextDouble();
                    } else if (task.getNumberOfIterations() < 2 * maxIterations / 4 && task.getNumberOfIterations() >= maxIterations / 4) {
                        double val = population.get(RNG.nextInt(popSize)).getValue(j);
                        newPosition[i][j] = bestSolution.getValue(j) * val
                                * ES * RNG.nextDouble();
                    } else if (task.getNumberOfIterations() < 3 * maxIterations / 4 && task.getNumberOfIterations() >= 2 * maxIterations / 4) {
                        newPosition[i][j] = bestSolution.getValue(j) * P * RNG.nextDouble();
                    } else {
                        newPosition[i][j] = bestSolution.getValue(j) - Eta * Double.MIN_VALUE - R * RNG.nextDouble();
                    }
                }

                task.problem.makeFeasible(newPosition[i]);
                if (task.isStopCriterion())
                    break;
                NumberSolution<Double> newReptile = new NumberSolution<>(Util.toDoubleArrayList(newPosition[i]));

                task.eval(newReptile);

                if (task.problem.isFirstBetter(newReptile, reptile)) {
                    population.set(i, newReptile);
                    updateBestSolution();
                }
            }
            task.incrementNumberOfIterations();
        }
        return bestSolution;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.add(task.generateRandomEvaluatedSolution());
        }
        updateBestSolution();
    }

    private void updateBestSolution() {
        bestSolution = population.get(0);
        for (int i = 1; i < population.size(); i++) {
            NumberSolution<Double> solution = population.get(i);
            if (task.problem.isFirstBetter(solution, bestSolution)) {
                bestSolution = solution;
            }
        }
    }

    private double mean(NumberSolution<Double> solution) {
        double sum = 0.0;
        for (int i = 0; i < solution.numberOfVariables(); i++) {
            sum += solution.getValue(i);
        }
        return sum / solution.numberOfVariables();
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
