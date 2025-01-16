package org.um.feri.ears.problems.unconstrained.cec2017;

public class F12 extends CEC2017 {

    // Hybrid function 3
    public F12(int d) {
        super("F12", d, 12);
    }

    @Override
    public double eval(double[] x) {
        return hf03(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}
