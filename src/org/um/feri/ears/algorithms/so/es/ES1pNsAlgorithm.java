package org.um.feri.ears.algorithms.so.es;

import org.um.feri.ears.algorithms.*;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.List;

public class ES1pNsAlgorithm extends NumberAlgorithm {
    private NumberSolution<Double> one;
    private double varianceOne;
    private int k, mem_k, n; // every k aVariance is calculated again
    private double c, mem_c;

    // source http://natcomp.liacs.nl/EA/slides/es_basic_algorithm.pdf
    public ES1pNsAlgorithm() {
        this(40, 0.8, 10);
    }

    public ES1pNsAlgorithm(int k, double c, int n) {
        mem_k = k;
        mem_c = c;
        this.n = n;
        au = new Author("matej", "matej.crepinsek@um.si");
        resetToDefaultsBeforeNewRun();
        ai = new AlgorithmInfo(
                "ES(1+N)", "ES(1+N)", //ES(1+1) 1/5 rule
                "@book{Rechenberg1973,\n author = {Rechenberg, I.}, \n publisher = {Frommann-Holzboog}, \n title = {Evolutionsstrategie: optimierung technischer systeme nach prinzipien der biologischen evolution},\n year = {1973}}"
         );
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        k = mem_k; // every k is recalculated
        c = mem_c; // 0.8<=c<=1
        varianceOne = 1.;
        one = null;

    }

    private double getGaussian(double aMean, double aVariance) {
        return aMean + RNG.nextGaussian() * aVariance;
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        resetToDefaultsBeforeNewRun(); // usually no need for this call
        this.task = task;
        NumberSolution<Double> ii;
        one = task.getRandomEvaluatedSolution();
        NumberSolution<Double> oneTmp;
        int everyK = 0; // recalculate variance
        double succ = 0;
        double[] oneplus;
        if (debug)
            System.out.println(task.getNumberOfEvaluations() + " start " + one);
        while (!task.isStopCriterion()) {
            everyK++;
            everyK = everyK % k;
            if (everyK == 0) { // 1/5 rule
                if ((succ / k) > 0.2)
                    varianceOne = varianceOne / c;
                else if ((succ / k) < 0.2)
                    varianceOne = varianceOne * c;
                succ = 0;
            }
            oneTmp = new NumberSolution<>(one);
            for (int i = 0; i < n; i++) {
                oneplus = Util.toDoubleArray(oneTmp.getVariables());
                mutate(oneplus, varianceOne);

                ii = new NumberSolution<>(Util.toDoubleArrayList(oneplus));
                task.eval(ii);

                if (task.problem.isFirstBetter(ii, one)) {
                    succ++; // for 1/5 rule
                    one = ii;
                    if (debug)
                        System.out.println(task.getNumberOfEvaluations() + " " + one);
                }
                if (task.isStopCriterion()) break;
            }
            task.incrementNumberOfIterations();
        }
        return one;
    }

    private void mutate(double[] oneplus, double varianceOne) {
        for (int i = 0; i < oneplus.length; i++) {
            oneplus[i] = task.problem.setFeasible(oneplus[i] + getGaussian(0, varianceOne), i);
        }

    }

    @Override
    public List<Algorithm> getAlgorithmParameterTest(int dimension, int maxCombinations) {
        List<Algorithm> alternative = new ArrayList<Algorithm>();
        if (maxCombinations == 1) {
            alternative.add(this);
        } else {
            double[][] paramCombinations = { // {k, c}
                    {40, 0.8, 1}, {40, 0.85, 2}, {30, 0.8, 5}, {20, 0.8, 4}, {40, 0.9, 6}, {20, 0.8, 15}, {40, 0.9, 10}};
            int counter = 0;
            for (int i = 0; (i < paramCombinations.length) && (counter < maxCombinations); i++) {
                alternative.add(new ES1pNsAlgorithm((int) paramCombinations[i][0], paramCombinations[i][1], (int) paramCombinations[i][2]));
                counter++;
            }
        }
        return alternative;
    }

}
