package org.um.feri.ears.algorithms.so.eflo;

import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.comparator.ProblemComparator;
import org.um.feri.ears.util.random.RNG;

import java.util.Arrays;
import java.util.ArrayList;

public class EFLO extends NumberAlgorithm {

    private int popSize;

    private ArrayList<NumberSolution<Double>> population;
    private NumberSolution<Double> bestSolution;

    public EFLO() {
        this(30);
    }

    public EFLO(int popSize) {
        this.popSize = popSize;
        ai = new AlgorithmInfo("EFLO", "Enhanced Frilled Lizard Optimizer", "EFLO");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {

        this.task = task;

        initPopulation(task);

        int maxIt = 10000;
        if (task.getStopCriterion() == StopCriterion.ITERATIONS) {
            maxIt = task.getMaxIterations();
        }
        if (task.getStopCriterion() == StopCriterion.EVALUATIONS) {
            maxIt = (int) ((task.getMaxEvaluations() - popSize) / (popSize * 2.5));
        }

        updateBest();

        int itCounter;
        while (!task.isStopCriterion()) {
            updateBest();

            itCounter = task.getNumberOfIterations() + 1;

            double frillThreshold = 1e-4;
            double frillStd = computeFrillStd();
            double frillFactor = 0.8 * Math.sqrt(1 - (double) itCounter / maxIt) + 0.1 * Math.sin((double) itCounter / maxIt) - 0.1;
            if (frillStd < frillThreshold) {
                frillFactor *= 0.5;
            }
            frillFactor = Math.max(0, frillFactor);

            for (int i = 0; i < popSize; i++) {
                NumberSolution<Double> agent = population.get(i);
                double[] newPosition = new double[task.problem.getNumberOfDimensions()];

                // Hunt
                NumberSolution<Double> selectedPrey = selectPrey(agent);
                double ri = RNG.nextDouble();
                int I = (int) Math.round(1 + ri);

                for (int d = 0; d < newPosition.length; d++) {
                    double r = RNG.nextDouble();
                    newPosition[d] = agent.getValue(d) + frillFactor * r * (selectedPrey.getValue(d) - I * agent.getValue(d));
                }

                task.problem.makeFeasible(newPosition);

                agent = tryReplaceAgent(i, agent, newPosition);

                // Climb
                double[] climbPosition = new double[task.problem.getNumberOfDimensions()];
                double[] climbImpulse = new double[task.problem.getNumberOfDimensions()];

                for (int d = 0; d < climbPosition.length; d++) {
                    climbImpulse[d] = (1 - 2 * RNG.nextDouble()) * ((task.problem.getUpperLimit(d) - task.problem.getLowerLimit(d)) / (double) itCounter) * frillFactor;
                }

                int[] indices = new int[popSize - 1];
                int idx = 0;
                for (int j = 0; j < popSize; j++)
                    if (j != i) {
                        indices[idx] = j;
                        idx++;
                    }

                RNG.shuffle(indices);

                int climbIdx1 = indices[0];
                int climbIdx2 = indices[1];

                for (int d = 0; d < climbPosition.length; d++) {
                    climbPosition[d] = agent.getValue(d) + climbImpulse[d] + (population.get(climbIdx1).getValue(d) - population.get(climbIdx2).getValue(d));
                }

                task.problem.makeFeasible(climbPosition);

                agent = tryReplaceAgent(i, agent, climbPosition);


                // local search
                double medianFitness = computeMedianFitness();
                if (agent.getEval() <= medianFitness) {

                    double[] localPosition = new double[task.problem.getNumberOfDimensions()];
                    for (int d = 0; d < localPosition.length; d++) {
                        double rg = RNG.nextGaussian();
                        localPosition[d] = agent.getValue(d) + frillFactor * rg;
                    }
                    task.problem.makeFeasible(localPosition);

                    tryReplaceAgent(i, agent, localPosition);
                }
            }
            task.incrementNumberOfIterations();
        }
        updateBest();
        return bestSolution;
    }

    private void initPopulation(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population.add(task.generateRandomEvaluatedSolution());
        }
        updateBest();
    }

    private void updateBest() {
        for (NumberSolution<Double> s : population) {
            if (task.problem.isFirstBetter(s, bestSolution)) {
                bestSolution = s.copy();
            }
        }
    }

    private NumberSolution<Double> tryReplaceAgent(int index, NumberSolution<Double> current, double[] candidatePosition) throws StopCriterionException {

        if (task.isStopCriterion()) return current;

        NumberSolution<Double> candidate = new NumberSolution<>(Util.toDoubleArrayList(candidatePosition));
        task.eval(candidate);
        if (task.problem.isFirstBetter(candidate, current)) {
            population.set(index, candidate);
            return candidate;
        }
        return current;
    }


    private double computeFrillStd() {
        double mean = 0.0;
        for (NumberSolution<Double> s : population) {
            mean += s.getEval();
        }
        mean /= population.size();
        double frillStd = 0.0;
        for (NumberSolution<Double> s : population) {
            double diff = s.getEval() - mean;
            frillStd += diff * diff;
        }
        frillStd = Math.sqrt(frillStd / population.size());

        return frillStd;
    }

    private NumberSolution<Double> selectPrey(NumberSolution<Double> agent) {
        ArrayList<NumberSolution<Double>> betterAgents = new ArrayList<>();
        for (NumberSolution<Double> s : population) {
            if (task.problem.isFirstBetter(s, agent)) {
                betterAgents.add(s);
            }
        }

        if (betterAgents.isEmpty()) {
            return bestSolution;
        }

        betterAgents.sort(new ProblemComparator<>(task.problem));
        int topK = Math.min(5, betterAgents.size());
        int randIndex = RNG.nextInt(topK);
        return betterAgents.get(randIndex);
    }

    private double computeMedianFitness() {
        double[] fitness = new double[population.size()];
        for (int i = 0; i < population.size(); i++) {
            fitness[i] = population.get(i).getEval();
        }
        Arrays.sort(fitness);
        int mid;
        if (fitness.length % 2 == 0) {
            mid = (fitness.length) / 2;
            return ((fitness[mid - 1] + fitness[mid]) / 2);
        } else {
            mid = (fitness.length - 1) / 2;
            return fitness[mid];
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}

