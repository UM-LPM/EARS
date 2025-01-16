package org.um.feri.ears.problems.unconstrained.cec2017;

public class F21 extends CEC2017 {

    // Composition function 2
    public F21(int d) {
        super("F21", d, 21);
    }

    @Override
    public double eval(double[] x) {
        return cf02(x, numberOfDimensions, 1) + 100.0 * funcNum;
    }
}
