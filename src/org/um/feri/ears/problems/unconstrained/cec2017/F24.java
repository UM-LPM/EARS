package org.um.feri.ears.problems.unconstrained.cec2017;

public class F24 extends CEC2017 {

    // Composition function 5
    public F24(int d) {
        super("F24", d, 24);
    }

    @Override
    public double eval(double[] x) {
        return cf05(x, numberOfDimensions, 1) + 100.0 * funcNum;
    }
}