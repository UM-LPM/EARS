package org.um.feri.ears.problems.unconstrained.cec2017;

public class F7 extends CEC2017 {

    // Shifted and Rotated Non-Continuous Rastrigin’s Function
    public F7(int d) {
        super("F07", d, 7);
    }

    @Override
    public double eval(double[] x) {
        return stepRastriginFunc(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}