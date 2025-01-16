package org.um.feri.ears.problems.unconstrained.cec2017;

public class F26 extends CEC2017 {

    // Composition function 7
    public F26(int d) {
        super("F26", d, 26);
    }

    @Override
    public double eval(double[] x) {
        return cf07(x, numberOfDimensions, 1) + 100.0 * funcNum;
    }
}
