package org.um.feri.ears.problems.unconstrained.cec2022;

public class F2 extends CEC2022 {

    // Shifted and Rotated Rosenbrock's Function
    public F2(int d) {
        super("F02", d, 2);
    }

    @Override
    public double eval(double[] x) {
        return rosenbrockFunc(x, numberOfDimensions, 1, 1, 0) + 400.0;
    }
}
