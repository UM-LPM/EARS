package org.um.feri.ears.problems.unconstrained.cec2017;

public class F8 extends CEC2017 {

    // Shifted and RotatedLevyFunction
    public F8(int d) {
        super("F08", d, 8);
    }

    @Override
    public double eval(double[] x) {
        return levyFunc(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}