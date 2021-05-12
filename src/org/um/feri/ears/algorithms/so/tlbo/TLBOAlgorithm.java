package org.um.feri.ears.algorithms.so.tlbo;

import org.um.feri.ears.algorithms.*;
import org.um.feri.ears.benchmark.EnumBenchmarkInfoParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Comparator.TaskComparator;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.annotation.AlgorithmParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

public class TLBOAlgorithm extends Algorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    private Task task;

    private int gen;
    private DoubleSolution[] population;

    private static boolean removeDuplicates = true;
    public Statistic stat;
    private static boolean useTF = true;
    private static boolean useAll4Mean = true;// used for internal tests
    private int dimensions;
    private double[] lowerLimit;
    private double[] upperLimit;
    private ArrayList<DoubleSolution> keepList;
    public static boolean test = false;

    private int keep = 0; // copy best from ex generation

    public TLBOAlgorithm() {
        this(20);
    }

    public TLBOAlgorithm(int popSize) {
        this(0, popSize);
    }

    public TLBOAlgorithm(int keep, int popSize) {
        this.keep = keep;
        this.popSize = popSize;
        au = new Author("matej", "matej.crepinsek@um.si");
        ai = new AlgorithmInfo(
                "TLBO", "Teaching Learning Based Optimization",
                "\\bibitem{Rao2011}\nR.V.~Rao, V.J.~Savsani, D.P.~Vakharia.\n\\newblock Teaching-learning-based optimization: A novel method for constrained mechanical design optimization problems.\n\\newblock \\emph{Computer-Aided Design}, 43(3):303--315, 2011."
        );
        ai.addParameter(EnumAlgorithmParameters.ELITE, "" + keep);
        ai.addParameter(EnumAlgorithmParameters.POP_SIZE, popSize + "");

    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriterionException {
        task = taskProblem;
        dimensions = task.getNumberOfDimensions();
        // max_eval = task.getMaxEvaluations();
        stat = new Statistic();
        init();
        aTeacher();

        return stat.getCurrent_g().best;
    }

    private double[] mean() {
        double[] d = new double[dimensions];
        int min;
        if (useAll4Mean)
            min = 0;
        else
            min = (int) (popSize * 0.075);
        int max = popSize - min;
        for (int i = min; i < max; i++) {
            for (int j = 0; j < dimensions; j++) {
                d[j] += population[i].getValue(j);
            }
        }
        for (int j = 0; j < dimensions; j++) {
            d[j] = d[j] / (max - min);
        }
        return d;
    }

    /**
     * Implemented by code Close to, but not 100% duplicates clear
     */
    private void clearDuplicates() throws StopCriterionException {
        double[] tmp1 = new double[dimensions];
        double[] tmp2 = new double[dimensions];
        double[] tmp3;
        for (int i = 0; i < popSize; i++) {
            for (int j = i + 1; j < popSize; j++) {
                if (task.isStopCriterion())
                    return; // end jump out
                System.arraycopy(population[i].getDoubleVariables(), 0, tmp1, 0, dimensions);
                System.arraycopy(population[j].getDoubleVariables(), 0, tmp2, 0, dimensions);
                Arrays.sort(tmp1);
                Arrays.sort(tmp2);
                if (Arrays.equals(tmp1, tmp2)) {
                    // on random place change value
                    stat.getCurrent_g().incDouple();
                    int pos = Util.rnd.nextInt(dimensions);
                    tmp3 = population[j].getDoubleVariables();

                    tmp3[pos] = Util.nextDouble(lowerLimit[pos], upperLimit[pos]);
                    StopCriterionException.id = " 3";
                    population[j] = task.eval(tmp3);

                }
            }
        }
    }

    private void sortByFirstBetterCondition() {
        TaskComparator s = new TaskComparator(task);
        Arrays.sort(population, s);
    }

    private void init() throws StopCriterionException {

        population = new DoubleSolution[popSize];
        lowerLimit = task.getLowerLimit();
        upperLimit = task.getUpperLimit();
        DoubleSolution best = task.getRandomEvaluatedSolution();
        population[0] = best;
        for (int i = 1; i < popSize; i++) {
            population[i] = task.getRandomEvaluatedSolution();
            if (task.isFirstBetter(population[i], best))
                best = population[i];

            if (task.isStopCriterion()) {
                //set best if global optimum reached or not enough evaluations to init population
                stat.getCurrent_g().setBest(best);
                return;
            }
        }

        if (removeDuplicates) {
            clearDuplicates(); // stop condition inside
        }

        sortByFirstBetterCondition();
        stat.getCurrent_g().setBest(population[0]);
        keepList = new ArrayList<>();
    }

    private void aTeacher() throws StopCriterionException {
        int TF = 1;
        double[] M;
        double[] tmpX;
        double[] tmpY;
        double[] tmpIsland;
        double[] newMean = new double[dimensions];
        double[] difMean = new double[dimensions];
        double[][] popTmp = new double[popSize][dimensions];
        DoubleSolution[] evalTmp = new DoubleSolution[popSize];
        DoubleSolution[] island1 = new DoubleSolution[popSize];
        gen = 0;
        DoubleSolution bestEvalCond = stat.getBest();
        while (!task.isStopCriterion()) { // generation or evaluations
            stat.newGeneration(gen);
            M = mean();
            if (test)
                System.out.println("mean M=" + Arrays.toString(M));
            newMean = population[0].getDoubleVariables();
            // Keep not in paper
            for (int k = 0; k < keep; k++)
                keepList.add(new DoubleSolution(population[k]));
            // Teacher phase
            // For every dimension it calculates dif_mean
            for (int n = 0; n < dimensions; n++) {
                if (useTF)
                    TF = Util.rnd.nextInt(2) + 1; // in source code is fix to 1
                difMean[n] = Util.rnd.nextDouble() * (newMean[n] - TF * M[n]);
            }
            if (test)
                System.out.println("difMean difMean=" + Arrays.toString(difMean));
            for (int i = 0; i < popSize; i++) {
                if (task.isStopCriterion())
                    break; // in loop after incEval
                tmpX = population[i].getDoubleVariables();
                for (int n = 0; n < dimensions; n++) {
                    popTmp[i][n] = task.setFeasible(tmpX[n] + difMean[n], n);
                }
                evalTmp[i] = task.eval(popTmp[i]);
                if (task.isFirstBetter(evalTmp[i], bestEvalCond))
                    bestEvalCond = evalTmp[i]; // 4 stop condition
                stat.incEval();
                if (test)
                    System.out.println("Compare new=" + Arrays.toString(popTmp[i]));
                if (test)
                    System.out.println("Compare old=" + population[i]);
                if (task.isFirstBetter(evalTmp[i], population[i])) {
                    stat.incUpdateByTeacher();
                    population[i] = evalTmp[i];
                }
            }
            // Learner phase
            int ii;
            int i_first = 0;
            for (; i_first < popSize; i_first++) {
                if (task.isStopCriterion())
                    break; // in loop after incEval
                ii = Util.rnd.nextInt(popSize);
                while (i_first == ii)
                    ii = Util.rnd.nextInt(popSize); // select different pair i, ii
                double rand = Util.rnd.nextDouble();
                if (test)
                    System.out.println("Rand=" + rand);
                if (test)
                    System.out.println("\nBasic " + population[i_first]);
                if (test)
                    System.out.println("Learning partner " + population[ii]);
                tmpX = population[i_first].getDoubleVariables();
                tmpY = population[ii].getDoubleVariables();
                tmpIsland = new double[dimensions];
                if (task.isFirstBetter(population[i_first], population[ii])) {
                    for (int n = 0; n < dimensions; n++) {
                        tmpIsland[n] = task.setFeasible(tmpX[n] + rand * (tmpX[n] - tmpY[n]), n);
                    }
                } else {
                    for (int n = 0; n < dimensions; n++) {
                        tmpIsland[n] = task.setFeasible(tmpX[n] + rand * (tmpY[n] - tmpX[n]), n);
                    }
                }
                if (test)
                    System.out.println("New " + Arrays.toString(tmpIsland));
                island1[i_first] = task.eval(tmpIsland);
                if (task.isFirstBetter(island1[i_first], bestEvalCond))
                    bestEvalCond = island1[i_first]; // for stop condition
                // ...epsilon no
                // constraints checked
                stat.incEval();
            }
            // i_first instead pop_size because of possible eval stop condition!
            for (int i = 0; i < i_first; i++) { // copy best
                if (task.isFirstBetter(island1[i], population[i])) {
                    population[i] = island1[i];
                }
            }
            sortByFirstBetterCondition();
            // Keep back change worst chromosomes
            int back = popSize - 1 - keep;
            // int back= (int) (pop_size*0.9);
            for (int k = 0; k < keep; k++)
                population[back + k] = keepList.get(k);
            keepList.clear();
            if (TLBOAlgorithm.removeDuplicates) {
                clearDuplicates(); // stop condition inside
            }
            sortByFirstBetterCondition();

            stat.getCurrent_g().setBest(population[0]);
            gen++;
            task.incrementNumberOfIterations();
        }
    }

    @Override
    public List<AlgorithmBase> getAlgorithmParameterTest(EnumMap<EnumBenchmarkInfoParameters, String> param, int maxCombinations) {
        List<AlgorithmBase> alternative = new ArrayList<>();
        int dim = 10;
        String sd = param.get(EnumBenchmarkInfoParameters.DIMENSION);
        if (sd != null) dim = Integer.parseInt(sd);
        if (maxCombinations == 1) {
            alternative.add(this);
        } else {
            int[][] paramCombinations = { // {elite, pop_size}
                    {4, 5 + dim * 2}, {0, 20}, {4, 20}, {0, 5 + dim * 2}, {0, 50}, {4, 50}, {0, 100}, {4, 100},
                    {8, 100}};
            int counter = 0;
            for (int i = 0; (i < paramCombinations.length) && (counter < maxCombinations); i++) {
                alternative.add(new TLBOAlgorithm(paramCombinations[i][0], paramCombinations[i][1]));
                counter++;

            }
        }
        return alternative;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
    }
}
