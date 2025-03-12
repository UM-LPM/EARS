package org.um.feri.ears.problems.unconstrained.cec2017;

public class F15 extends CEC2017 {

    // Hybrid function 6
    public F15(int d) {
        super("F15", d, 15);
    }

    @Override
    public double eval(double[] x) {
        return hf06(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}