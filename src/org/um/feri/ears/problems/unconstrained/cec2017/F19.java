package org.um.feri.ears.problems.unconstrained.cec2017;

public class F19 extends CEC2017 {

    // Hybrid function 10
    public F19(int d) {
        super("F19", d, 19);
    }

    @Override
    public double eval(double[] x) {
        return hf10(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}