package org.um.feri.ears.problems.unconstrained.cec2017;

public class F22 extends CEC2017 {

    // Composition function 3
    public F22(int d) {
        super("F22", d, 22);
    }

    @Override
    public double eval(double[] x) {
        return cf03(x, numberOfDimensions, 1) + 100.0 * funcNum;
    }
}