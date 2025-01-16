package org.um.feri.ears.problems.unconstrained.cec2017;

public class F14 extends CEC2017 {

    // Hybrid function 5
    public F14(int d) {
        super("F14", d, 14);
    }

    @Override
    public double eval(double[] x) {
        return hf05(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}