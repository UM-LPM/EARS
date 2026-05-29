package org.um.feri.ears.problems.unconstrained.cec2022;

public class F4 extends CEC2022 {

    // Non-Continuous Rotated Rastrigin's Function
    public F4(int d) {
        super("F04", d, 4);
    }

    @Override
    public double eval(double[] x) {
        return stepRastriginFunc(x, numberOfDimensions, 1, 1, 0) + 800.0;
    }
}
