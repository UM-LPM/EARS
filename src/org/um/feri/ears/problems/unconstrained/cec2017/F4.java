package org.um.feri.ears.problems.unconstrained.cec2017;

public class F4 extends CEC2017 {

    // Shifted and Rotated Rastrigin’s Function
    public F4(int d) {
        super("F04", d, 4);
    }

    @Override
    public double eval(double[] x) {
        return rastriginFunc(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}
