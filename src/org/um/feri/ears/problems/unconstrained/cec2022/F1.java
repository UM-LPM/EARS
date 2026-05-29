package org.um.feri.ears.problems.unconstrained.cec2022;

public class F1 extends CEC2022 {

    // Shifted and Rotated Zakharov Function
    public F1(int d) {
        super("F01", d, 1);
    }

    @Override
    public double eval(double[] x) {
        return zakharovFunc(x, numberOfDimensions, 1, 1, 0) + 300.0;
    }
}

