package org.um.feri.ears.problems.unconstrained.cec2022;

public class F5 extends CEC2022 {

    // Shifted and Rotated Levy Function
    public F5(int d) {
        super("F05", d, 5);
    }

    @Override
    public double eval(double[] x) {
        return levyFunc(x, numberOfDimensions, 1, 1, 0) + 900.0;
    }
}
