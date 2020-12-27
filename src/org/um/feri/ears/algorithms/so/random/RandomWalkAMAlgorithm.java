package org.um.feri.ears.algorithms.so.random;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.DoubleSolution;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;

/**
 * Similar as Random walk only that in case of finding new best the arithmetic
 * mean of individuals is new best and prior best is calculated and tested. Also
 * new in same direction is tested.
 * <p>
 *
 * @author Matej Crepinsek
 * @version 1
 *
 * <h3>License</h3>
 * <p>
 * Copyright (c) 2011 by Matej Crepinsek. <br>
 * All rights reserved. <br>
 *
 * <p>
 * ution and use in source and binary forms, with or without
 * ion, are permitted provided that the following conditions
 * are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * <li>Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * <li>Neither the name of the copyright owners, their employers, nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 * </ul>
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
public class RandomWalkAMAlgorithm extends Algorithm {
    private DoubleSolution bestSolution;
    private Task task;

    public RandomWalkAMAlgorithm() {
        this.debug = false;
        ai = new AlgorithmInfo("RWS", "", "RWAM", "Random Walk Arithmetic");
        au = new Author("matej", "matej.crepinsek at um.si");
    }


    private double[] xArithmeticMeanOf(double[] x, double[] y) {
        double[] am = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            am[i] = (x[i] + y[i]) * 0.5;
        }
        return am;
    }

    private double[] xInSameDirection(double[] old, double[] newX) {
        double[] am = new double[old.length];
        for (int i = 0; i < old.length; i++) {
            am[i] = task.setFeasible(newX[i] + (newX[i] - old[i]), i); // if out of
            // range
        }
        return am;
    }

    @Override
    public DoubleSolution execute(Task taskProblem) throws StopCriterionException {
        DoubleSolution currentSolution, iAritmetic, iExtend;
        task = taskProblem;
        bestSolution = taskProblem.getRandomEvaluatedSolution();
        if (debug)
            System.out.println(taskProblem.getNumberOfEvaluations() + " " + bestSolution);
        while (!taskProblem.isStopCriterion()) {
            currentSolution = taskProblem.getRandomEvaluatedSolution();
            if (taskProblem.isFirstBetter(currentSolution, bestSolution)) {
                if (!taskProblem.isStopCriterion()) { // try also arithmetic mean
                    iAritmetic = taskProblem.eval(xArithmeticMeanOf(bestSolution.getDoubleVariables(), currentSolution.getDoubleVariables()));
                    if (taskProblem.isFirstBetter(iAritmetic, currentSolution)) {
                        currentSolution = iAritmetic; // even better
                    } else {
                        if (!taskProblem.isStopCriterion()) { // try also extend
                            iExtend = taskProblem.eval(xInSameDirection(bestSolution.getDoubleVariables(), currentSolution.getDoubleVariables()));
                            if (taskProblem.isFirstBetter(iExtend, currentSolution)) {
                                currentSolution = iExtend; // even better
                            }
                        }
                    }
                }
                bestSolution = currentSolution;
                if (debug)
                    System.out.println(taskProblem.getNumberOfEvaluations() + " " + bestSolution);
            }
            task.incrementNumberOfIterations();
        }
        return bestSolution;
    }


    @Override
    public void resetToDefaultsBeforeNewRun() {
        bestSolution = null;
    }

}
