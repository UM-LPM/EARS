package org.um.feri.ears.problems.unconstrained.cec2017;

public class F11 extends CEC2017 {

    // Hybrid function 2
    public F11(int d) {
        super("F11", d, 11);
    }

    @Override
    public double eval(double[] x) {
        return hf02(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}