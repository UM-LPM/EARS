package org.um.feri.ears.problems.unconstrained.cec2017;

public class F28 extends CEC2017 {

    // Composition function 9
    public F28(int d) {
        super("F28", d, 28);
    }

    @Override
    public double eval(double[] x) {
        return cf09(x, numberOfDimensions, 1) + 100.0 * funcNum;
    }
}
