package org.um.feri.ears.problems.unconstrained.cec2017;

public class F16 extends CEC2017 {

    // Hybrid function 7
    public F16(int d) {
        super("F16", d, 16);
    }

    @Override
    public double eval(double[] x) {
        return hf07(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}