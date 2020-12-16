package org.um.feri.ears.problems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.util.Util;

/**
 * Main common class for constrained and unconstrained problems.
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
public abstract class Problem extends ProblemBase<Double> {

    protected double[][] optimum;
    protected int numberOfGlobalOptima = 1;

    public Problem(int numberOfDimensions, int numberOfConstraints, int numberOfGlobalOptima) {
        super(numberOfDimensions, numberOfConstraints);

        this.numberOfGlobalOptima = numberOfGlobalOptima;

        optimum = new double[numberOfGlobalOptima][numberOfDimensions];
        Arrays.fill(optimum[0], 0); // default global optimum is at [0, 0, ...., 0, 0]
    }

    public Problem(int numberOfDimensions, int numberOfConstraints) {
        this(numberOfDimensions, numberOfConstraints, 1);
    }

    /**
     * Checks if the provided vector is inside the interval given by the upper and lower limits
     *
     * @param x vector to be checked
     * @return true if the vector is inside interval, false otherwise
     */
    public boolean areDimensionsInFeasibleInterval(List<Double> x) {
        for (int i = 0; i < numberOfDimensions; i++) {
            if (x.get(i) < lowerLimit.get(i))
                return false;
            if (x.get(i) > upperLimit.get(i))
                return false;
        }
        return true;
    }

    /**
     * Checks if the {@code value} is inside upper and lower limit for the {@code dimension}.
     * If the {@code value} is greater than upper limit it is set to upper limit.
     * If the {@code value} is smaller than lower limit it is set to lower limit.
     * If the {@code value} is inside the interval, the original value is returned.
     *
     * @param value     to be set feasible
     * @param dimension of the interval
     * @return feasible value
     * @CheckReturnValue
     */
    public double setFeasible(double value, int dimension) {
        if (value < lowerLimit.get(dimension))
            return lowerLimit.get(dimension);
        if (value > upperLimit.get(dimension))
            return upperLimit.get(dimension);
        return value;
    }

    /**
     * Sets every variable in {@code double[] x} feasible.
     *
     * @param x vector to be set to feasible
     * @return vector containing feasible variables
     */
    public double[] setFeasible(double[] x) {
        for (int i = 0; i < x.length; i++) {
            x[i] = setFeasible(x[i], i);
        }
        return x;
    }

    /**
     * Sets every variable in {@code double[] x} feasible.
     *
     * @param x vector to be set to feasible
     * @return vector containing feasible variables
     */
    public List<Double> setFeasible(List<Double> x) {
        for (int i = 0; i < x.size(); i++) {
            x.set(i, setFeasible(x.get(i), i));
        }
        return x;
    }

    /**
     * Checks if the {@code value} is inside upper and lower limit for the {@code dimension}.
     *
     * @param value     to be checked if feasible
     * @param dimension of the interval
     * @return true if value feasible, false otherwise
     */
    public boolean isFeasible(double value, int dimension) {
        if (value < lowerLimit.get(dimension) || value > upperLimit.get(dimension))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Problem: " + name + " version: " + version + " dimensions: " + numberOfDimensions + " constraints: " + numberOfConstraints;
    }

    public static final EnumProblemTypes TYPE = EnumProblemTypes.SORPO;

    /**
     * Returns a 2 dimensional vector containing all the global optima.
     *
     * @return vector containing the global optima
     */
    public final double[][] getOptimalVector() {
        return optimum;
    }

    /**
     * Returns the number of global optima.
     *
     * @return number of global optima
     */
    public final int getNumberOfGlobalOptima() {
        return numberOfGlobalOptima;
    }

    /**
     * Implements the problem's fitness function.
     *
     * @param x variables to evaluate
     * @return fitness value
     */
    public abstract double eval(double[] x);

    public final double eval(List<Double> x) {
        return eval(x.stream().mapToDouble(i -> i).toArray());
    }

    public final double eval(Double[] x) {
        return eval(ArrayUtils.toPrimitive(x));
    }

    public boolean isFirstBetter(double a, double b) {
        if (minimize)
            return a < b;
        return a > b;
    }

    /**
     * Returns the global minimum or maximum.
     *
     * @return value of global optimum.
     */
    public double getGlobalOptimum() {
        return 0;
    }

    /**
     * Override this method if the problem has constraints.
     *
     * @param x variables for which the constraints will be evaluated
     * @return computed constraints
     */
    public double[] computeConstraints(double[] x) {
        return new double[0];
    }

    public final double[] computeConstraints(List<Double> x) {
        return computeConstraints(x.stream().mapToDouble(i -> i).toArray());
    }

    public final double[] computeConstraints(Double[] x) {
        return computeConstraints(ArrayUtils.toPrimitive(x));
    }

    /**
     * @param x - solution
     * @return
     */
    public double evaluateConstraints(List<Double> x) {
        if (numberOfConstraints == 0)
            return 0;
        double[] g = computeConstraints(x); //calculate for every constrain (problem depended)
        double d = 0;
        for (int j = 0; j < numberOfConstraints; j++) {
            if (g[j] > 0) {
                if (constrained_type == CONSTRAINED_TYPE_COUNT) d++;
                if (constrained_type == CONSTRAINED_TYPE_SUM) d += g[j];
                if (constrained_type == CONSTRAINED_TYPE_NORMALIZATION)
                    d += g[j] * normalization_constraints_factor[j];// *(count_constrains[j]+1);

            }
        }
        return d;
    }

    /**
     * Generates a random evaluated solution.
     *
     * @return generated solution
     */
    public DoubleSolution getRandomSolution() {
        //Double[] var=new Double[numberOfDimensions];
        ArrayList<Double> var = new ArrayList<Double>();
        for (int j = 0; j < numberOfDimensions; j++) {
            //var[j] = Util.nextDouble(lowerLimit.get(j), upperLimit.get(j));
            var.add(Util.nextDouble(lowerLimit.get(j), upperLimit.get(j)));
        }
        return new DoubleSolution(var, eval(var), computeConstraints(var), upperLimit, lowerLimit);
    }

    /**
     * Generates random feasible variables
     *
     * @return random variables
     */
    public double[] getRandomVariables() {
        double[] var = new double[numberOfDimensions];
        for (int j = 0; j < numberOfDimensions; j++) {
            var[j] = Util.nextDouble(lowerLimit.get(j), upperLimit.get(j));
        }
        return var;
    }

    /**
     * Compares fitness values and constraints
     *
     * @param first
     * @param firstEval
     * @param second
     * @param secondEval
     * @return
     */
    public boolean isFirstBetter(List<Double> first, double firstEval, List<Double> second, double secondEval) {
        double firstCons = evaluateConstraints(first);
        double secondCons = evaluateConstraints(second);
        if (firstCons == 0) {
            if (secondCons == 0) {
                if (minimize)
                    return firstEval < secondEval;
                return firstEval > secondEval;
            }
            return true; // second is not feasible
        }
        if (secondCons == 0) {
            return false;
        }
        return firstCons < secondCons; // less constraints is better

    }
}
