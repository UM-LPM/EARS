package org.um.feri.ears.problems;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.util.random.RNG;

import javax.annotation.CheckReturnValue;
import java.util.ArrayList;
import java.util.List;

public abstract class DoubleProblem extends NumberProblem<Double> {

    public DoubleProblem(String name, int numberOfDimensions, int numberOfGlobalOptima, int numberOfObjectives, int numberOfConstraints) {
        super(name, numberOfDimensions, numberOfGlobalOptima, numberOfObjectives, numberOfConstraints);
    }

    @Override
    public void makeFeasible(NumberSolution<Double> solution) {
        setFeasible(solution.getVariables());
    }

    @Override
    public boolean isFeasible(NumberSolution<Double> solution) {
        return isFeasible(solution.getVariables());
    }

    @Override
    public NumberSolution<Double> getRandomSolution() {

        List<Double> x = new ArrayList<>();
        for (int j = 0; j < numberOfDimensions; j++) {
            x.add(RNG.nextDouble(lowerLimit.get(j), upperLimit.get(j)));
        }

        return new NumberSolution<>(numberOfObjectives, x);
    }

    /**
     * Generates random feasible variables
     *
     * @return random variables
     */
    public double[] getRandomVariables() {
        double[] x = new double[numberOfDimensions];
        for (int j = 0; j < numberOfDimensions; j++) {
            x[j] = RNG.nextDouble(lowerLimit.get(j), upperLimit.get(j));
        }
        return x;
    }

    public double[] getInterval() {
        double[] interval = new double[upperLimit.size()];
        for (int i = 0; i < interval.length; i++) {
            interval[i] = upperLimit.get(i) - lowerLimit.get(i);
        }
        return interval;
    }

    /**
     * Sets every variable in {@code double[] x} feasible.
     *
     * @param x vector to be set to feasible
     */
    public void setFeasible(List<Double> x) {
        for (int i = 0; i < x.size(); i++) {
            x.set(i, setFeasible(x.get(i), i));
        }
    }

    public void setFeasible(double[] x) {
        for (int i = 0; i < x.length; i++) {
            x[i] = setFeasible(x[i], i);
        }
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
     */
    @CheckReturnValue
    public double setFeasible(double value, int dimension) {
        return Math.max(Math.min(value, upperLimit.get(dimension)), lowerLimit.get(dimension));
    }

    /**
     * Checks if the {@code value} is inside upper and lower limit for the {@code dimension}.
     *
     * @param value     to be checked if feasible
     * @param dimension of the interval
     * @return true if value feasible, false otherwise
     */
    public boolean isFeasible(double value, int dimension) {
        return (value >= lowerLimit.get(dimension) && value <= upperLimit.get(dimension));
    }

    /**
     * Checks if the provided array is inside the interval given by the upper and lower limits
     *
     * @param x array to be checked
     * @return true if the array is inside interval, false otherwise
     */
    public boolean isFeasible(List<Double> x) {
        for (int i = 0; i < numberOfDimensions; i++) {
            if (!isFeasible(x.get(i), i))
                return false;
        }
        return true;
    }

    //TODO replace with accessor pattern
    @Override
    public void evaluate(NumberSolution<Double> solution) {
        solution.setObjective(0, eval(solution.getVariables()));
    }

    //TODO make abstract and protected
    /**
     * Implements the problem's fitness function.
     *
     * @param x variables to evaluate
     * @return fitness value
     */
    public double eval(double[] x) {return Double.MAX_VALUE;}

    public final double eval(List<Double> x) {
        return eval(x.stream().mapToDouble(i -> i).toArray());
    }

    public final double eval(Double[] x) {
        return eval(ArrayUtils.toPrimitive(x));
    }
}
