package org.um.feri.ears.algorithms.so.es;

import org.um.feri.ears.algorithms.*;
import org.um.feri.ears.benchmark.EnumBenchmarkInfoParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class ES1cNsAlgorithm extends Algorithm {
    private DoubleSolution one;
    private double varianceOne;
    private int k, mem_k, n; // every k aVariance is calculated again
    private double c, mem_c;

    private Task task;

    // source http://natcomp.liacs.nl/EA/slides/es_basic_algorithm.pdf
    public ES1cNsAlgorithm() {
        this(40, 0.8, 10);
    }

    public ES1cNsAlgorithm(int k, double c, int n) {
        mem_k = k;
        mem_c = c;
        this.n = n;
        au = new Author("matej", "matej.crepinsek at um.si");
        resetToDefaultsBeforeNewRun();
        ai = new AlgorithmInfo(
                "ES",
                "@book{Rechenberg1973,\n author = {Rechenberg, I.}, \n publisher = {Frommann-Holzboog}, \n title = {Evolutionsstrategie: optimierung technischer systeme nach prinzipien der biologischen evolution},\n year = {1973}}",
                "ES(1,N)", "ES(1+1) 1/5 rule");
        ai.addParameter(EnumAlgorithmParameters.K_ITERATIONS, "" + k);
        ai.addParameter(EnumAlgorithmParameters.C_FACTOR, "" + c);
        ai.addParameter(EnumAlgorithmParameters.MU, "" + n);

    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        k = mem_k; // every k is recalculated
        c = mem_c; // 0.8<=c<=1
        varianceOne = 1.;
        one = null;

    }

    private double getGaussian(double aMean, double aVariance) {
        return aMean + Util.rnd.nextGaussian() * aVariance;
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriterionException {
        resetToDefaultsBeforeNewRun(); // usually no need for this call
        task = taskProblem;
        DoubleSolution ii;
        one = taskProblem.getRandomEvaluatedSolution();
        DoubleSolution oneTmp;
        int everyK = 0; // recalculate variance
        double succ = 0;
        double[] oneplus;
        if (debug)
            System.out.println(taskProblem.getNumberOfEvaluations() + " start " + one);
        while (!taskProblem.isStopCriterion()) {
            everyK++;
            everyK = everyK % k;
            if (everyK == 0) { // 1/5 rule
                if ((succ / k) > 0.2)
                    varianceOne = varianceOne / c;
                else if ((succ / k) < 0.2)
                    varianceOne = varianceOne * c;
                succ = 0;
            }
            oneTmp = new DoubleSolution(one);
            oneplus = oneTmp.getDoubleVariables();
            mutate(oneplus, varianceOne);
            one = taskProblem.eval(oneplus);
            for (int i = 0; i < n - 1; i++) {
                oneplus = oneTmp.getDoubleVariables();
                mutate(oneplus, varianceOne);
                ii = taskProblem.eval(oneplus);
                if (taskProblem.isFirstBetter(ii, one)) {
                    succ++; // for 1/5 rule
                    one = ii;
                    if (debug)
                        System.out.println(taskProblem.getNumberOfEvaluations() + " " + one);
                }
                if (task.isStopCriterion()) break;
            }
            task.incrementNumberOfIterations();
        }
        return one;
    }

    private void mutate(double[] oneplus, double varianceOne) {
        for (int i = 0; i < oneplus.length; i++) {
            oneplus[i] = task.setFeasible(oneplus[i] + getGaussian(0, varianceOne), i);
        }

    }

    @Override
    public List<AlgorithmBase> getAlgorithmParameterTest(EnumMap<EnumBenchmarkInfoParameters, String> param, int maxCombinations) {
        List<AlgorithmBase> alternative = new ArrayList<AlgorithmBase>();
        if (maxCombinations == 1) {
            alternative.add(this);
        } else {
            double[][] paramCombinations = { // {k, c}
                    {40, 0.8, 5}, {40, 0.85, 2}, {30, 0.8, 5}, {20, 0.8, 4}, {40, 0.9, 6}, {20, 0.8, 15}, {40, 0.9, 10}};
            int counter = 0;
            for (int i = 0; (i < paramCombinations.length) && (counter < maxCombinations); i++) {
                alternative.add(new ES1cNsAlgorithm((int) paramCombinations[i][0], paramCombinations[i][1], (int) paramCombinations[i][2]));
                counter++;
            }
        }
        return alternative;
    }

}
