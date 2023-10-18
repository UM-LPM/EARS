package org.um.feri.ears.algorithms.so.soma;

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

public class SOMA extends NumberAlgorithm {

    public enum Strategy {ALL_TO_ALL, ALL_TO_ALL_ADAPTIVE, ALL_TO_ONE, ALL_TO_ONE_RANDOM}

    @AlgorithmParameter(name = "population size",
            min = "10")
    private int popSize;
    @AlgorithmParameter(name = "path length",
            description = "defines how far an individual stops behind the Leader",
            min = "1.1",
            max = "5")
    private double pathLength;
    @AlgorithmParameter(name = "step",
            description = "defines the granularity with what the search space is sampled",
            min = "0.11",
            max = "pathLength")
    private double step;
    @AlgorithmParameter(name = "perturbation",
            description = "determines whether an individual will travel directly towards the Leader, or not",
            min = "0",
            max = "1")
    private double prt;
    @AlgorithmParameter(name = "strategy")
    private Strategy strategy;

    private NumberSolution<Double> best;
    private int leaderId; //index of the best solution

    private NumberSolution<Double>[] population;

    public SOMA() {
        this(Strategy.ALL_TO_ALL);
    }

    public SOMA(int popSize) {
        this(Strategy.ALL_TO_ALL, popSize);
    }

    public SOMA(Strategy strategy) {
        this(strategy, 50);
    }

    public SOMA(Strategy strategy, int popSize) {
        this(strategy, popSize, 0.3, 3.1, 0.3);
    }

    public SOMA(Strategy strategy, int popSize, double step, double pathLength, double prt) {
        super();
        this.strategy = strategy;
        this.popSize = popSize;
        this.step = step;
        this.pathLength = pathLength;
        this.prt = prt;

        // if step parameter is larger than pathLength it will cause an infinite loop
        assert (step <= pathLength);

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("SOMA_" + strategy.name(), "Self-Organizing Migrating Algorithm " + strategy.name(),
                "@article{zelinka2016soma,"
                        + "title={SOMA—self-organizing migrating algorithm},"
                        + "author={Zelinka, Ivan},"
                        + "booktitle={Self-Organizing Migrating Algorithm},"
                        + "pages={3--49},"
                        + "year={2016},"
                        + "publisher={Springer}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        initPopulation();

        while (!task.isStopCriterion()) {

            switch (strategy) {
                case ALL_TO_ALL:
                    population = allToAll();
                    break;
                case ALL_TO_ONE:
                    population = allToOne();
                    break;
                case ALL_TO_ONE_RANDOM:
                    population = allToOneRandom();
                    break;
                case ALL_TO_ALL_ADAPTIVE:
                    population = allToOneAdaptive();
                    break;
            }
            updateBestSolution();
            task.incrementNumberOfIterations();
        }
        return best;
    }

    private NumberSolution<Double>[] allToAll() throws StopCriterionException {
        NumberSolution<Double>[] newPopulation = new NumberSolution[popSize];
        for (int i = 0; i < popSize; i++) {
            ArrayList<NumberSolution<Double>> solutions = new ArrayList<>();
            for (int j = 0; j < popSize; j++) {
                if (i != j) {
                    solutions.addAll(getSolutionsOnJumpingPositions(population[i], population[j]));
                }
            }
            newPopulation[i] = getBestSolution(solutions);
        }
        return newPopulation;
    }

    private NumberSolution<Double>[] allToOne() throws StopCriterionException {
        NumberSolution<Double>[] newPopulation = new NumberSolution[popSize];
        updateBestSolution();
        for (int i = 0; i < popSize; i++) {
            if (i == leaderId) {
                newPopulation[i] = new NumberSolution<>(best);
            } else {
                ArrayList<NumberSolution<Double>> soluitions = getSolutionsOnJumpingPositions(population[i], best);
                newPopulation[i] = getBestSolution(soluitions);
            }
        }
        return newPopulation;
    }

    private NumberSolution<Double>[] allToOneRandom() throws StopCriterionException {
        NumberSolution<Double>[] newPopulation = new NumberSolution[popSize];
        int leaderId = RNG.nextInt(popSize);
        NumberSolution<Double> leader = population[leaderId];
        for (int i = 0; i < popSize; i++) {
            if (i == leaderId) {
                newPopulation[i] = new NumberSolution<>(leader);
            } else {
                ArrayList<NumberSolution<Double>> solutions = getSolutionsOnJumpingPositions(population[i], leader);
                newPopulation[i] = getBestSolution(solutions);
            }
        }
        return newPopulation;
    }

    private NumberSolution<Double>[] allToOneAdaptive() throws StopCriterionException {
        NumberSolution<Double>[] newPopulation = new NumberSolution[popSize];
        for (int i = 0; i < popSize; i++) {
            NumberSolution<Double> jumpingSolution = population[i];
            for (int j = 0; j < popSize; j++) {
                if (i != j) {
                    ArrayList<NumberSolution<Double>> solutions = getSolutionsOnJumpingPositions(jumpingSolution, population[j]);
                    jumpingSolution = getBestSolution(solutions);
                }
            }
            newPopulation[i] = jumpingSolution;
        }
        return newPopulation;
    }

    private void updateBestSolution() {
        for (int i = 0; i < popSize; i++) {
            if (task.problem.isFirstBetter(population[i], best)) {
                best = new NumberSolution<>(population[i]);
                leaderId = i;
            }
        }
    }

    private void initPopulation() throws StopCriterionException {
        population = new NumberSolution[popSize];
        best = task.getRandomEvaluatedSolution();
        leaderId = 0;
        population[0] = new NumberSolution<>(best);
        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population[i] = task.getRandomEvaluatedSolution();
            if (task.problem.isFirstBetter(population[i], best)) {
                best = new NumberSolution<>(population[i]);
                leaderId = i;
            }
        }
    }

    private NumberSolution<Double> getBestSolution(ArrayList<NumberSolution<Double>> solutions) {
        NumberSolution<Double> bestSolution = solutions.get(0);
        for (int i = 1; i < solutions.size(); i++) {
            if (solutions.get(i) == null)
                continue;
            if (task.problem.isFirstBetter(solutions.get(i), bestSolution)) {
                bestSolution = solutions.get(i);
            }
        }
        return bestSolution;
    }

    private ArrayList<NumberSolution<Double>> getSolutionsOnJumpingPositions(NumberSolution<Double> jumpingSolution, NumberSolution<Double> towardsSolution) throws StopCriterionException {
        ArrayList<NumberSolution<Double>> solutions = new ArrayList<>();
        solutions.add(jumpingSolution);
        for (int i = 1; i * step <= pathLength; i++) {
            if (task.isStopCriterion())
                return solutions;
            NumberSolution<Double> addSolution = getSolutionOnStep(jumpingSolution, towardsSolution, i * step);
            solutions.add(addSolution);
        }
        return solutions;
    }

    private NumberSolution<Double> getSolutionOnStep(NumberSolution<Double> jumpingSolution, NumberSolution<Double> towardsSolution, double jump) throws StopCriterionException {
        double[] newSolution = new double[task.problem.getNumberOfDimensions()];
        boolean[] prtVector = createPrtVector();
        for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
            if (prtVector[i]) {
                newSolution[i] = jumpingSolution.getValue(i) + (towardsSolution.getValue(i) - jumpingSolution.getValue(i)) * jump;
                while (newSolution[i] < task.problem.getLowerLimit(i)) {
                    newSolution[i] += task.problem.getUpperLimit(i) - task.problem.getLowerLimit(i);
                }
                while (newSolution[i] > task.problem.getUpperLimit(i)) {
                    newSolution[i] -= task.problem.getUpperLimit(i) - task.problem.getLowerLimit(i);
                }
            } else {
                newSolution[i] = jumpingSolution.getValue(i);
            }
        }

        NumberSolution<Double> solution = new NumberSolution<>(Util.toDoubleArrayList(newSolution));
        task.eval(solution);

        return solution;
    }

    private boolean[] createPrtVector() {
        boolean[] prtVector = new boolean[task.problem.getNumberOfDimensions()];
        boolean ok = false;
        do {
            for (int i = 0; i < prtVector.length; i++) {
                prtVector[i] = RNG.nextDouble() < prt;
                if (prtVector[i]) {
                    ok = true;
                }
            }
        } while (!ok);
        return prtVector;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}