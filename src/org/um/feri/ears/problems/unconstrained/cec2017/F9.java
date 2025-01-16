package org.um.feri.ears.problems.unconstrained.cec2017;

public class F9 extends CEC2017 {

    // Shifted and Rotated Schwefel’s Function
    public F9(int d) {
        super("F09", d, 9);
    }

    @Override
    public double eval(double[] x) {
        return schwefelFunc(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}
