package org.um.feri.ears.problems.unconstrained.cec2017;

public class F20 extends CEC2017 {

    // Composition function 1
    public F20(int d) {
        super("F20", d, 20);
    }

    @Override
    public double eval(double[] x) {
        return cf01(x, numberOfDimensions, 1) + 100.0 * funcNum;
    }
}
