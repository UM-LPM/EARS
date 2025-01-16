package org.um.feri.ears.problems.unconstrained.cec2017;

public class F27 extends CEC2017 {

    // Composition function 8
    public F27(int d) {
        super("F27", d, 27);
    }

    @Override
    public double eval(double[] x) {
        return cf08(x, numberOfDimensions, 1) + 100.0 * funcNum;
    }
}
