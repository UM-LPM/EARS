package org.um.feri.ears.algorithms.so.hsa;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

public class HSA extends Algorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter(description = "harmony memory considering rate")
    private double HMCR = 0.99;
    @AlgorithmParameter(description = "pitch adjusting Rate")
    private double PAR = 0.5;
    @AlgorithmParameter(description = "band width")
    private double BW = 0.5;

    private DoubleSolution best;
    private Task task;
    private DoubleSolution[] population;

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
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");
    }

    @Override
    public DoubleSolution execute(Task task) throws StopCriterionException {
        this.task = task;

        initPopulation();
        while (!this.task.isStopCriterion()) {
            double[] newHarmony = new double[this.task.getNumberOfDimensions()];

            for (int i = 0; i < this.task.getNumberOfDimensions(); i++) {
                if (Util.nextDouble() < HMCR) {
                    newHarmony[i] = population[(int) (popSize * Util.nextDouble())].getValue(i);
                    if (Util.nextDouble() < PAR) {
                        newHarmony[i] = pitchAdjust(newHarmony[i], i);
                    }
                } else {
                    newHarmony[i] = this.task.getLowerLimit(i) + (this.task.getUpperLimit(i) - this.task.getLowerLimit(i)) * Util.nextDouble();
                }
            }
            if (this.task.isStopCriterion())
                break;

            DoubleSolution newSolution = this.task.eval(newHarmony);
            //updateHarmonyMemory(newScore, newHarmony);

            //find worst harmony
            int worstIndex = 0;
            for (int i = 1; i < popSize; i++) {
                if (this.task.isFirstBetter(population[worstIndex], population[i]))
                    worstIndex = i;
            }

            if (this.task.isFirstBetter(newSolution, population[worstIndex])) {
                population[worstIndex] = newSolution;
            }
            //find best harmony
            if (this.task.isFirstBetter(newSolution, best)) {
                best = new DoubleSolution(newSolution);
            }
            this.task.incrementNumberOfIterations();
        }
        return best;
    }

    private double pitchAdjust(double value, int dimension) {
        double newValue;
        if (Util.nextDouble() < 0.5) {
            newValue = value + Util.nextDouble() * BW;
        } else {
            newValue = value - Util.nextDouble() * BW;
        }
        return task.setFeasible(newValue, dimension);
    }

    private void initPopulation() throws StopCriterionException {
        population = new DoubleSolution[popSize];

        best = task.getRandomEvaluatedSolution();
        population[0] = new DoubleSolution(best);
        for (int i = 1; i < popSize; i++) {
            if (task.isStopCriterion())
                break;
            population[i] = task.getRandomEvaluatedSolution();
            if (task.isFirstBetter(population[i], best)) {
                best = new DoubleSolution(population[i]);
            }
        }
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }
}
