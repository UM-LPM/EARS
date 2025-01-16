package org.um.feri.ears.problems.unconstrained.cec2017;

public class F13 extends CEC2017 {

    // Hybrid function 4
    public F13(int d) {
        super("F13", d, 13);
    }

    @Override
    public double eval(double[] x) {
        return hf04(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}