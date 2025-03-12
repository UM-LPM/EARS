package org.um.feri.ears.problems.unconstrained.cec2017;

public class F18 extends CEC2017 {

    // Hybrid function 9
    public F18(int d) {
        super("F18", d, 18);
    }

    @Override
    public double eval(double[] x) {
        return hf09(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}