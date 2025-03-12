package org.um.feri.ears.problems.unconstrained.cec2017;

public class F17 extends CEC2017 {

    // Hybrid function 8
    public F17(int d) {
        super("F17", d, 17);
    }

    @Override
    public double eval(double[] x) {
        return hf08(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}