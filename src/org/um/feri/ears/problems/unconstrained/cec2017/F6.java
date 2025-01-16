package org.um.feri.ears.problems.unconstrained.cec2017;

public class F6 extends CEC2017 {

    // Shifted and Rotated Lunacek Bi-Rastrigin’s Function
    public F6(int d) {
        super("F06", d, 6);
    }

    @Override
    public double eval(double[] x) {
        return biRastriginFunc(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}