package org.um.feri.ears.algorithms.so.tlbo;

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

import java.util.Arrays;

import static java.util.Arrays.stream;

public class OSSTLBO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private NumberSolution<Double> best;
    private NumberSolution<Double>[] population;

    private int m = 0;

    public OSSTLBO() {
        this(20);
    }

    public OSSTLBO(int popSize) {

        this.popSize = popSize;
        //https://github.com/MohammadJomaa/OSS-TLBO
        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("OSSTLBO", "Opposite Stopping Swarm Teaching–learning-Based Optimization",
                "@article{jomaa2014opposite,"
                        + "title={Opposite Stopping Swarm Teaching–learning-Based Optimization},"
                        + "author={Jomaa, Mohammad and Juneidi, Wassim },"
                        + "booktitle={Higher Institute for Applied Sciences and Technology}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        initPopulation();

        while (!task.isStopCriterion()) {

            teacherPhase();
            learnerPhase();
            updatePopulation();
            task.incrementNumberOfIterations();
        }

        return best;
    }

    private void initPopulation() throws StopCriterionException {
        population = new NumberSolution[popSize];

        best = task.getRandomEvaluatedSolution();
        population[0] = new NumberSolution<>(best);
        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population[i] = task.getRandomEvaluatedSolution();
            if (task.problem.isFirstBetter(population[i], best)) {
                best = new NumberSolution<>(population[i]);
            }
        }
        dynamicOppositeLearning();
    }

    private void dynamicOppositeLearning() throws StopCriterionException {
        for (int i = 0; i < popSize; i++) {
            double[] newX = new double[task.problem.getNumberOfDimensions()];
            for (int n = 0; n < task.problem.getNumberOfDimensions(); n++) {
                double X = population[i].getValue(n);
                double XO = (task.problem.getLowerLimit(n) + task.problem.getUpperLimit(n) - X);
                double XOD = X + RNG.nextDouble() * (RNG.nextDouble() * XO - X);
                if (XOD < task.problem.getLowerLimit(n) || XOD > task.problem.getUpperLimit(n)) {
                    XOD = RNG.nextInt((int) (task.problem.getUpperLimit(n) - task.problem.getLowerLimit(n)));
                }
                newX[n] = XOD;
            }
            if (task.isStopCriterion())
                return;

            NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newX));
            task.eval(newSolution);

            if (task.problem.isFirstBetter(newSolution, population[i])) {
                population[i] = newSolution;
                if (task.problem.isFirstBetter(newSolution, best)) {
                    best = new NumberSolution<>(newSolution);
                }
            }
        }
    }

    private void teacherPhase() throws StopCriterionException {

        for (int i = 0; i < popSize; i++) {
            double[] newX = new double[task.problem.getNumberOfDimensions()];
            double[] means = calculateXMean();
            for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                double Tf = RNG.nextInt(1, 3);
                double differenceMean = RNG.nextDouble() * (best.getValue(j) - Tf * means[j]);
                double differenceTeacherStudent = RNG.nextDouble() * (best.getValue(j) - population[i].getValue(j));
                double x = population[i].getValue(j) + differenceMean + differenceTeacherStudent;

                if (!task.problem.isFeasible(x, j)) {
                    x = RNG.nextDouble(task.problem.getLowerLimit(j), task.problem.getUpperLimit(j));
                }
                newX[j] = x;
            }

            if (task.isStopCriterion())
                return;

            NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newX));
            task.eval(newSolution);

            if (task.problem.isFirstBetter(newSolution, population[i])) {
                population[i] = newSolution;

                if (task.problem.isFirstBetter(newSolution, best)) {
                    best = new NumberSolution<>(newSolution);
                }
            }
        }
    }

    private void learnerPhase() throws StopCriterionException {

        for (int i = 0; i < popSize; i++) {
            NumberSolution<Double> solution = population[i];
            NumberSolution<Double> randomSolution = population[RNG.nextInt(popSize)];
            while (randomSolution == solution)
                randomSolution = population[RNG.nextInt(popSize)];

            double[] newX = new double[task.problem.getNumberOfDimensions()];

            if (task.problem.isFirstBetter(randomSolution, solution)) {
                for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                    double x = solution.getValue(j) + RNG.nextDouble() * (randomSolution.getValue(j) - (solution.getValue(j)));
                    if (!task.problem.isFeasible(x, j)) {
                        x = RNG.nextDouble(task.problem.getLowerLimit(j), task.problem.getUpperLimit(j));
                    }
                    newX[j] = x;
                }
            } else {
                for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                    double x = solution.getValue(j) + RNG.nextDouble() * (solution.getValue(j) - (randomSolution.getValue(j)));
                    if (!task.problem.isFeasible(x, j)) {
                        x = RNG.nextDouble(task.problem.getLowerLimit(j), task.problem.getUpperLimit(j));
                    }
                    newX[j] = x;
                }
            }

            if (task.isStopCriterion())
                return;

            NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newX));
            task.eval(newSolution);

            if (task.problem.isFirstBetter(newSolution, population[i])) {
                population[i] = newSolution;

                if (task.problem.isFirstBetter(newSolution, best)) {
                    best = new NumberSolution<>(newSolution);
                }
            }
        }
    }

    private void updatePopulation() throws StopCriterionException {
        Arrays.sort(population, new ProblemComparator<>(task.problem));
        m = m + 1;

        if (m == 20) {
            m = 0;
            dynamicOppositeLearning();
        }
    }

    private double[] calculateXMean() {
        double[] means = new double[task.problem.getNumberOfDimensions()];

        for (NumberSolution<Double> solution : population) {
            for (int j = 0; j < task.problem.getNumberOfDimensions(); j++) {
                means[j] += solution.getValue(j);
            }
        }
        return stream(means).map(d -> d / popSize).toArray();
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
