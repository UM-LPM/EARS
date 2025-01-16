package org.um.feri.ears.problems.unconstrained.cec2017;

public class F29 extends CEC2017 {

    // Composition function 10
    public F29(int d) {
        super("F29", d, 29);
    }

    @Override
    public double eval(double[] x) {
        return cf10(x, numberOfDimensions, 1) + 100.0 * funcNum;
    }
}