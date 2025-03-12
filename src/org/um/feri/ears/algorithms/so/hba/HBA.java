package org.um.feri.ears.algorithms.so.hba;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;

public class HBA extends NumberAlgorithm {
    @AlgorithmParameter(name = "population size")
    private int popSize;

    private static final int BETA = 6;
    private static final int C = 2;
    private static final double EPS = Math.ulp(1.0);

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> bestSolution;

    public HBA() {
        this(30);
    }

    public HBA(int popSize) {
        super();
        this.popSize = popSize;

        au = new Author("crt", "crt.gorican@student.um.si");
        ai = new AlgorithmInfo("HBA", "Honey Badger Algorithm",
                "@article{article,"
                    + "author = {Hashim, Fatma and Houssein, Essam and Talpur, Kashif and Mabrouk, Mai and Al-Atabany, Walid},"
                    + "year = {2022},"
                    + "month = {01},"
                    + "pages = {},"
                    + "title = {Honey Badger Algorithm: New Metaheuristic Algorithm for Solving Optimization Problems},"
                    + "volume = {192},"
                    + "journal = {Mathematics and Computers in Simulation},"
                    + "doi = {10.1016/j.matcom.2021.08.013}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        int maxIterations = 1_000;

        initPopulation();

        int bestIndex = getBestSolutionIndex();
        bestSolution = population.get(bestIndex);

        int[] flagVec = {1, -1};

        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIterations = task.getMaxIterations();
        } else if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIterations = (task.getMaxEvaluations() - popSize) / popSize;
        }

        while (!task.isStopCriterion()) {
            int iteration = task.getNumberOfIterations() + 1;
            // alpha = C * exp(-t / tmax); %density factor in Eq. (3)
            double alpha = C * Math.exp(-(double)iteration / (double)maxIterations);
            // I = Intensity(N, Xprey, X); %intensity in Eq. (2)
            ArrayList<Double> intensity = getPopulationIntensity(bestSolution);

            ArrayList<NumberSolution<Double>> populationNew = new ArrayList<>(popSize);
            for (NumberSolution<Double> solution : population) {
                populationNew.add(solution.copy());
            }

            for (int i=0; i<popSize && !task.isStopCriterion(); i++) {

                double r = RNG.nextDouble();

                double f = flagVec[(int) Math.floor(RNG.nextDouble(0.0, 2.0))];

                for (int j = 0; j < population.get(i).getVariables().size(); j++) {
                    double di = bestSolution.getValue(j) - population.get(i).getValue(j);

                    if (r < .5) {
                        double[] rVec = {
                            RNG.nextDouble(),
                            RNG.nextDouble(),
                            RNG.nextDouble()
                        };

                        // Xnew(i, j) = Xprey(j)
                        // + F * beta * I(i) * Xprey(j)
                        // + F * r3 * alpha * (di) * abs(cos(2 * pi * r4) * (1 - cos(2 * pi * r5)));
                        double value = bestSolution.getValue(j)
                            + f * BETA * intensity.get(i) * bestSolution.getValue(j)
                            + f * rVec[0] * alpha * di * Math.abs(Math.cos(2.0 * Math.PI * rVec[1])) * (1.0 - Math.cos(2.0 * Math.PI * rVec[2]));
                        populationNew.get(i).setValue(j, value);
                    }
                    else {
                        double r7 = RNG.nextDouble(0.0, 1.0);
                        // Xnew(i, j) = Xprey(j) + F * r7 * alpha * di;
                        double value = bestSolution.getValue(j) + f * r7 * alpha * di;
                        populationNew.get(i).setValue(j, value);
                    }
                }

                if(!task.problem.isFeasible(populationNew.get(i).getVariables())) {
                    task.problem.makeFeasible(populationNew.get(i).getVariables());
                }

                if (task.isStopCriterion())
                    break;
                task.eval(populationNew.get(i));
                if(task.problem.isFirstBetter(populationNew.get(i), population.get(i))) {
                    population.set(i, populationNew.get(i).copy());
                }
            }

            // [Ybest, index] = min(fitness);
            int bestIndexNew = getBestSolutionIndex();
            NumberSolution<Double> bestSolutionNew = population.get(bestIndexNew);

            if(task.problem.isFirstBetter(bestSolutionNew, bestSolution)) {
                bestSolution = bestSolutionNew;
            }
            task.incrementNumberOfIterations();
        }

        return bestSolution;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>(popSize);

        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.add(task.generateRandomEvaluatedSolution());
        }
    }

    private int getBestSolutionIndex() {
        int bestIndex = 0;
        NumberSolution<Double> bestSolution = population.get(0);

        for (int i = 1; i < popSize; i++) {
            if (task.problem.isFirstBetter(population.get(i), bestSolution)) {
                bestIndex = i;
                bestSolution = population.get(i);
            }
        }

        return bestIndex;
    }

    private ArrayList<Double> getPopulationIntensity(NumberSolution<Double> xPrey) {
        ArrayList<Double> intensity = new ArrayList<>(popSize);
        {
            ArrayList<Double> s = new ArrayList<>(popSize);
            ArrayList<Double> di = new ArrayList<>(popSize);
            double value;
            for (int i = 0; i < popSize-1; i++) {
                // di(i) =( norm((X(i,:)-Xprey+eps))).^2;
                value = normDifference(population.get(i).getVariables(), xPrey.getVariables());
                value = Math.pow(value, 2);
                di.add(value);
                // S(i)=( norm((X(i,:)-X(i+1,:)+eps))).^2;
                value = normDifference(population.get(i).getVariables(), population.get(i + 1).getVariables());
                value = Math.pow(value, 2);
                s.add(value);
            }

            // di(N)=( norm((X(N,:)-Xprey+eps))).^2;
            value = normDifference(population.get(popSize-1).getVariables(), xPrey.getVariables());
            value = Math.pow(value, 2);
            di.add(value);
            // S(N)=( norm((X(N,:)-X(1,:)+eps))).^2;
            value = normDifference(population.get(popSize-1).getVariables(), population.get(0).getVariables());
            value = Math.pow(value, 2);
            s.add(value);

            for (int i=0; i<popSize; i++) {
                // I(i)=r2*S(i)/(4*pi*di(i));
                value = RNG.nextDouble(0.0, 1.0) * s.get(i) / (4 * Math.PI * di.get(i));
                intensity.add(value);
            }
        }

        return intensity;
    }

    private static double normDifference(ArrayList<Double> vec1, ArrayList<Double> vec2) {
        double sum = 0.0;
        for (int i = 0; i < vec1.size(); i++) {
            double diff = vec1.get(i) - vec2.get(i) + EPS;
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}
