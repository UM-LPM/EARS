package org.um.feri.ears.algorithms.so.fdo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;


public class FDO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private Bee best;
    private Bee[] population;

    private boolean solutionMustRemainInsideBoundary = true;

    public FDO() {
        this(30);
    }

    public FDO(int popSize) {
        this.popSize = popSize;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("FDO", "Fitness Dependent Optimizer",
                "@article{abdullah2019fitness,"
                        + "title={Fitness dependent optimizer: inspired by the bee swarming reproductive process},"
                        + "author={Abdullah, Jaza Mahmood and Ahmed, Tarik},"
                        + "journal={IEEE Access},"
                        + "volume={7},"
                        + "pages={43473--43486},"
                        + "year={2019},"
                        + "publisher={IEEE}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        while (!task.isStopCriterion()) {
            for (int i = 0; i < popSize; i++) {
                double[] tempXs = new double[task.problem.getNumberOfDimensions()];
                ArrayList<Double> lastPace = new ArrayList<>();
                double fitnessWeight = 0.0;
                if (population[i].getEval() != 0) {
                    //generate fitness weight fW
                    if (task.problem.isFirstBetter(best.getEval(), (0.05 * population[i].getEval()), 0)) {
                        fitnessWeight = 0.2;
                    } else {
                        double weightFactor = 0.0;
                        fitnessWeight = (best.getEval() / population[i].getEval()) - weightFactor;
                    }
                }
                for (int dim = 0; dim < task.problem.getNumberOfDimensions(); dim++) {
                    double distanceFromBestBee = best.getValue(dim) - population[i].getValue(dim);
                    double x = population[i].getValue(dim);
                    double pace = 0.0;
                    double r = simpleRandomWalk();
                    if (fitnessWeight == 1) {
                        pace = x * r;
                    } else if (fitnessWeight == 0) {
                        pace = distanceFromBestBee * r;
                    } else {
                        pace = (distanceFromBestBee * fitnessWeight);
                        if (r < 0) {
                            pace = pace * -1;
                        }
                    }
                    if (Double.isInfinite(pace)) {
                        pace = x * r;
                    }
                    double newBeeXs = x + pace;
                    if (solutionMustRemainInsideBoundary) {
                        newBeeXs = setFeasible(newBeeXs, dim);
                    }
                    tempXs[dim] = newBeeXs;
                    lastPace.add(pace); //save pace for potential reuse
                }

                if (task.isStopCriterion())
                    break;
                // create temporary bee for comparison
                NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(tempXs));
                task.eval(newSolution);

                Bee tempBee = new Bee(newSolution);

                if (task.problem.isFirstBetter(tempBee, population[i])) {
                    population[i] = new Bee(tempBee);
                    population[i].setLastPace(lastPace);
                    if (task.problem.isFirstBetter(tempBee, best)) {
                        best = new Bee(tempBee);
                    }
                } else if (population[i].getLastPace().size() > 0) {
                    tempXs = new double[task.problem.getNumberOfDimensions()];
                    for (int n = 0; n < task.problem.getNumberOfDimensions(); n++) {
                        double distanceFromBestBee = best.getValue(n) - population[i].getValue(n);
                        double x = population[i].getValue(n) + (distanceFromBestBee * fitnessWeight) + population[i].getLastPace().get(n);
                        if (solutionMustRemainInsideBoundary) {
                            x = setFeasible(x, n);
                        }
                        tempXs[n] = x;
                    }
                    if (task.isStopCriterion())
                        break;

                    newSolution = new NumberSolution<>(Util.toDoubleArrayList(tempXs));
                    task.eval(newSolution);

                    tempBee = new Bee(newSolution);

                    if (task.problem.isFirstBetter(tempBee, population[i])) {
                        population[i] = new Bee(tempBee);
                        if (task.problem.isFirstBetter(tempBee, best)) {
                            best = new Bee(tempBee);
                        }
                    } else {
                        tempXs = new double[task.problem.getNumberOfDimensions()];

                        for (int n = 0; n < task.problem.getNumberOfDimensions(); n++) {
                            double r = simpleRandomWalk();
                            double x = population[i].getValue(n) + population[i].getValue(n) * r;

                            if (solutionMustRemainInsideBoundary) {
                                x = setFeasible(x, n);
                            }
                            tempXs[n] = x;
                        }

                        if (task.isStopCriterion())
                            break;

                        newSolution = new NumberSolution<>(Util.toDoubleArrayList(tempXs));
                        task.eval(newSolution);

                        tempBee = new Bee(newSolution);

                        if (task.problem.isFirstBetter(tempBee, population[i])) {
                            population[i] = new Bee(tempBee);
                            if (task.problem.isFirstBetter(tempBee, best)) {
                                best = new Bee(tempBee);
                            }
                            break;
                        }
                    }
                }
            }
            task.incrementNumberOfIterations();
        }

        return best;
    }

    private double simpleRandomWalk() {
        double beta = 0.5;
        double sigma = Math.pow((gamma(1 + beta) * Math.sin((Math.PI * beta) / 2) / (gamma((1 + beta) / 2) * beta * Math.pow(2, (beta - 1) / 2))), (1 / beta));
        double r1 = (0.1 * (RNG.nextDouble() * 2 - 1)) * sigma;
        double r2 = 0.1 * (RNG.nextDouble() * 2 - 1);
        double levyFlight = (r1 / Math.pow(Math.abs(r2), 1 / beta)) * 0.01;
        if (levyFlight >= 1.0 || levyFlight <= -1.0) {
            return simpleRandomWalk();
        } else {
            return levyFlight;
        }
    }

    private double logGamma(double x) {
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0 + 76.18009173 / (x + 0) - 86.50532033 / (x + 1)
                + 24.01409822 / (x + 2) - 1.231739516 / (x + 3)
                + 0.00120858003 / (x + 4) - 0.00000536382 / (x + 5);
        return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
    }

    private double gamma(double x) {
        return Math.exp(logGamma(x));
    }

    private double setFeasible(double x, int d) {
        if (x > task.problem.getUpperLimit(d)) {
            x = task.problem.getUpperLimit(d) * RNG.nextDouble();
        } else if (x < task.problem.getLowerLimit(d)) {
            x = task.problem.getLowerLimit(d) * RNG.nextDouble();
        }
        return x;
    }

    private void initPopulation() throws StopCriterionException {
        population = new Bee[popSize];
        best = new Bee(task.getRandomEvaluatedSolution());
        population[0] = new Bee(new NumberSolution(best));
        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population[i] = new Bee(task.getRandomEvaluatedSolution());
            if (task.problem.isFirstBetter(population[i], best)) {
                best = new Bee(population[i]);
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
