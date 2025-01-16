package org.um.feri.ears.problems.unconstrained.cec2017;

public class F2 extends CEC2017 {

    // Shifted and Rotated Zakharov Function
    public F2(int d) {
        super("F02", d, 2);
    }

    @Override
    public double eval(double[] x) {
        return zakharovFunc(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}
