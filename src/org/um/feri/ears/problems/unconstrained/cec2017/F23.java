package org.um.feri.ears.problems.unconstrained.cec2017;

public class F23 extends CEC2017 {

    // Composition function 4
    public F23(int d) {
        super("F23", d, 23);
    }

    @Override
    public double eval(double[] x) {
        return cf04(x, numberOfDimensions, 1) + 100.0 * funcNum;
    }
}