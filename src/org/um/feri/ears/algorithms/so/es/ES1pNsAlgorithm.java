package org.um.feri.ears.algorithms.so.es;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmBase;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;
import org.um.feri.ears.benchmark.EnumBenchmarkInfoParameters;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.util.Util;

/**
 * ES(1+1)
 * <p>
 * 
 * @author Matej Crepinsek
 * @version 1
 * 
 *          <h3>License</h3>
 * 
 *          Copyright (c) 2011 by Matej Crepinsek. <br>
 *          All rights reserved. <br>
 * 
 *          <p>
 *          Redistribution and use in source and binary forms, with or without
 *          modification, are permitted provided that the following conditions
 *          are met:
 *          <ul>
 *          <li>Redistributions of source code must retain the above copyright
 *          notice, this list of conditions and the following disclaimer.
 *          <li>Redistributions in binary form must reproduce the above
 *          copyright notice, this list of conditions and the following
 *          disclaimer in the documentation and/or other materials provided with
 *          the distribution.
 *          <li>Neither the name of the copyright owners, their employers, nor
 *          the names of its contributors may be used to endorse or promote
 *          products derived from this software without specific prior written
 *          permission.
 *          </ul>
 *          <p>
 *          THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *          "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *          LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *          FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *          COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *          INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *          BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *          LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *          CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *          LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *          ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *          POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class ES1pNsAlgorithm extends Algorithm {
    private DoubleSolution one;
    private double varianceOne;
    private int k, mem_k, n; // every k aVariance is calculated again
    private double c, mem_c;

    Task task;

    // source http://natcomp.liacs.nl/EA/slides/es_basic_algorithm.pdf
    public ES1pNsAlgorithm() {
        this(40, 0.8, 10);
    }

    public ES1pNsAlgorithm(int k, double c, int n) {
        mem_k = k;
        mem_c = c;
        this.n = n;
        au = new Author("matej", "matej.crepinsek at uni-mb.si");
        resetDefaultsBeforNewRun();
        ai = new AlgorithmInfo(
                "ES",
                "@book{Rechenberg1973,\n author = {Rechenberg, I.}, \n publisher = {Frommann-Holzboog}, \n title = {Evolutionsstrategie: optimierung technischer systeme nach prinzipien der biologischen evolution},\n year = {1973}}",
                "ES(1+N)", "ES(1+1) 1/5 rule");
        ai.addParameter(EnumAlgorithmParameters.K_ITERATIONS, "" + k);
        ai.addParameter(EnumAlgorithmParameters.C_FACTOR, "" + c);
        ai.addParameter(EnumAlgorithmParameters.MU, "" + n);

    }

    @Override
    public void resetDefaultsBeforNewRun() {
        k = mem_k; // every k is recalculated
        c = mem_c; // 0.8<=c<=1
        varianceOne = 1.;
        one = null;

    }

    private double getGaussian(double aMean, double aVariance) {
        return aMean + Util.rnd.nextGaussian() * aVariance;
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriteriaException {
        resetDefaultsBeforNewRun(); // usually no need for this call
        task = taskProblem;
        DoubleSolution ii;
        one = taskProblem.getRandomSolution();
        DoubleSolution oneTmp;
        int everyK = 0; // recalculate variance
        int succ = 0;
        double oneplus[];
        if (debug)
            System.out.println(taskProblem.getNumberOfEvaluations() + " start " + one);
        while (!taskProblem.isStopCriteria()) {
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
            for (int i = 0; i < n; i++) {
                oneplus = oneTmp.getNewVariables();
                mutate(oneplus, varianceOne);
                ii = taskProblem.eval(oneplus);
                if (taskProblem.isFirstBetter(ii, one)) {
                    succ++; // for 1/5 rule
                    one = ii;
                    if (debug)
                        System.out.println(taskProblem.getNumberOfEvaluations() + " " + one);
                }
                if (task.isStopCriteria()) break;
            }
        }
        return one;
    }

    /**
     * @param oneplus
     * @param varianceOne
     */
    private void mutate(double[] oneplus, double varianceOne) {
        for (int i = 0; i < oneplus.length; i++) {
            oneplus[i] = task.feasible(oneplus[i] + getGaussian(0, varianceOne), i);
        }

    }

    @Override
    public List<AlgorithmBase> getAlgorithmParameterTest(EnumMap<EnumBenchmarkInfoParameters, String> param, int maxCombinations) {
        List<AlgorithmBase> alternative = new ArrayList<AlgorithmBase>();
        if (maxCombinations == 1) {
            alternative.add(this);
        } else {
            double paramCombinations[][] = { // {k, c}
            { 40, 0.8, 1 }, { 40, 0.85, 2 }, { 30, 0.8, 5 }, { 20, 0.8, 4 }, { 40, 0.9, 6 }, { 20, 0.8, 15 }, { 40, 0.9, 10 } };
            int counter = 0;
            for (int i = 0; (i < paramCombinations.length) && (counter < maxCombinations); i++) {
                alternative.add(new ES1pNsAlgorithm((int) paramCombinations[i][0], paramCombinations[i][1], (int) paramCombinations[i][2]) );
                counter++;
            }
        }
        return alternative;
    }

}
