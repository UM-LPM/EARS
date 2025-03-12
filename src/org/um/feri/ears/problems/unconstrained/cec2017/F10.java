package org.um.feri.ears.problems.unconstrained.cec2017;

public class F10 extends CEC2017 {

    // Hybrid function 1
    public F10(int d) {
        super("F10", d, 10);
    }

    @Override
    public double eval(double[] x) {
        return hf01(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}
