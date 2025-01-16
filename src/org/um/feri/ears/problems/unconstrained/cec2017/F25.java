package org.um.feri.ears.problems.unconstrained.cec2017;

public class F25 extends CEC2017 {

    // Composition function 6
    public F25(int d) {
        super("F25", d, 25);
    }

    @Override
    public double eval(double[] x) {
        return cf06(x, numberOfDimensions, 1) + 100.0 * funcNum;
    }
}
