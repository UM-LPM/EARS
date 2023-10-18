package org.um.feri.ears.algorithms.so.hsa;

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

public class HSA extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter(description = "harmony memory considering rate")
    private double HMCR = 0.99;
    @AlgorithmParameter(description = "pitch adjusting Rate")
    private double PAR = 0.5;
    @AlgorithmParameter(description = "band width")
    private double BW = 0.5;
    private NumberSolution<Double> best;
     private NumberSolution<Double>[] population;

    public HSA() {
        this(50);
    }

    public HSA(int popSize) {

        this.popSize = popSize;

        au = new Author("miha", "miha.ravber@um.si");
        ai = new AlgorithmInfo("HSA", "Harmony Search Algorithm",
                "@article{kim2016harmony, " +
                        "title={Harmony search algorithm: A unique music-inspired algorithm}, " +
                        "author={Kim, Joong Hoon}, " +
                        "journal={Procedia engineering}, " +
                        "volume={154}, " +
                        "pages={1401--1405}, " +
                        "year={2016}, " +
                        "publisher={Elsevier}}"
        );
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;

        initPopulation();
        while (!task.isStopCriterion()) {
            double[] newHarmony = new double[task.problem.getNumberOfDimensions()];

            for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
                if (RNG.nextDouble() < HMCR) {
                    newHarmony[i] = population[(int) (popSize * RNG.nextDouble())].getValue(i);
                    if (RNG.nextDouble() < PAR) {
                        newHarmony[i] = pitchAdjust(newHarmony[i], i);
                    }
                } else {
                    newHarmony[i] = task.problem.getLowerLimit(i) + (task.problem.getUpperLimit(i) - task.problem.getLowerLimit(i)) * RNG.nextDouble();
                }
            }
            if (task.isStopCriterion())
                break;

            NumberSolution<Double> newSolution = new NumberSolution<>(Util.toDoubleArrayList(newHarmony));
            task.eval(newSolution);

            //updateHarmonyMemory(newScore, newHarmony);

            //find worst harmony
            int worstIndex = 0;
            for (int i = 1; i < popSize; i++) {
                if (task.problem.isFirstBetter(population[worstIndex], population[i]))
                    worstIndex = i;
            }

            if (task.problem.isFirstBetter(newSolution, population[worstIndex])) {
                population[worstIndex] = newSolution;
            }
            //find best harmony
            if (task.problem.isFirstBetter(newSolution, best)) {
                best = new NumberSolution<>(newSolution);
            }
            task.incrementNumberOfIterations();
        }
        return best;
    }

    private double pitchAdjust(double value, int dimension) {
        double newValue;
        if (RNG.nextDouble() < 0.5) {
            newValue = value + RNG.nextDouble() * BW;
        } else {
            newValue = value - RNG.nextDouble() * BW;
        }
        return task.problem.setFeasible(newValue, dimension);
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
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
