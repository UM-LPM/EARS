package org.um.feri.ears.algorithms.so.fdo;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;

public class FDO extends Algorithm {

    private Bee best;
    private Task task;
    private int popSize;
    private Bee[] population;

    private boolean solutionMustRemainInsideBoundary = true;

    public FDO() {
        this(30);
    }

    public FDO(int popSize) {
        this.popSize = popSize;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("FDO",
                "@article{abdullah2019fitness,"
                        + "title={Fitness dependent optimizer: inspired by the bee swarming reproductive process},"
                        + "author={Abdullah, Jaza Mahmood and Ahmed, Tarik},"
                        + "journal={IEEE Access},"
                        + "volume={7},"
                        + "pages={43473--43486},"
                        + "year={2019},"
                        + "publisher={IEEE}}",
                "FDO", "Fitness Dependent Optimizer");
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
        task = taskProblem;
        initPopulation();

        while (!task.isStopCriteria()) {
            for (int i = 0; i < popSize; i++) {
                double[] tempXs = new double[task.getNumberOfDimensions()];
                ArrayList<Double> lastPace = new ArrayList<>();
                double fitnessWeight = 0.0;
                if (population[i].getEval() != 0) {
                    //generate fitness weight fW
                    if (task.isFirstBetter(best.getEval(), (0.05 * population[i].getEval()))) {
                        fitnessWeight = 0.2;
                    } else {
                        double weightFactor = 0.0;
                        fitnessWeight = (best.getEval() / population[i].getEval()) - weightFactor;
                    }
                }
                for (int dim = 0; dim < task.getNumberOfDimensions(); dim++) {
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

                if (task.isStopCriteria())
                    break;
                // create temporary bee for comparision purpose
                Bee tempBee = new Bee(task.eval(tempXs));

                if (task.isFirstBetter(tempBee, population[i])) {
                    population[i] = new Bee(tempBee);
                    population[i].setLastPace(lastPace);
                    if (task.isFirstBetter(tempBee, best)) {
                        best = new Bee(tempBee);
                    }
                } else if (population[i].getLastPace().size() > 0) {
                    tempXs = new double[task.getNumberOfDimensions()];
                    for (int n = 0; n < task.getNumberOfDimensions(); n++) {
                        double distanceFromBestBee = best.getValue(n) - population[i].getValue(n);
                        double x = population[i].getValue(n) + (distanceFromBestBee * fitnessWeight) + population[i].getLastPace().get(n);
                        if (solutionMustRemainInsideBoundary) {
                            x = setFeasible(x, n);
                        }
                        tempXs[n] = x;
                    }
                    if (task.isStopCriteria())
                        break;
                    tempBee = new Bee(task.eval(tempXs));

                    if (task.isFirstBetter(tempBee, population[i])) {
                        population[i] = new Bee(tempBee);
                        if (task.isFirstBetter(tempBee, best)) {
                            best = new Bee(tempBee);
                        }
                    } else {
                        tempXs = new double[task.getNumberOfDimensions()];

                        for (int n = 0; n < task.getNumberOfDimensions(); n++) {
                            double r = simpleRandomWalk();
                            double x = population[i].getValue(n) + population[i].getValue(n) * r;

                            if (solutionMustRemainInsideBoundary) {
                                x = setFeasible(x, n);
                            }
                            tempXs[n] = x;
                        }

                        if (task.isStopCriteria())
                            break;
                        tempBee = new Bee(task.eval(tempXs));

                        if (task.isFirstBetter(tempBee, population[i])) {
                            population[i] = new Bee(tempBee);
                            if (task.isFirstBetter(tempBee, best)) {
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
        double r1 = (0.1 * (Util.nextDouble() * 2 - 1)) * sigma;
        double r2 = 0.1 * (Util.nextDouble() * 2 - 1);
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
        if (x > task.getUpperLimit(d)) {
            x = task.getUpperLimit(d) * Util.nextDouble();
        } else if (x < task.getLowerLimit(d)) {
            x = task.getLowerLimit(d) * Util.nextDouble();
        }
        return x;
    }

    private void initPopulation() throws StopCriteriaException {
        population = new Bee[popSize];
        best = new Bee(task.getRandomSolution());
        population[0] = new Bee(new DoubleSolution(best));
        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriteria())
                break;
            population[i] = new Bee(task.getRandomSolution());
            if (task.isFirstBetter(population[i], best)) {
                best = new Bee(population[i]);
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
