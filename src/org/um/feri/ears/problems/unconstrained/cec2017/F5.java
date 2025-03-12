package org.um.feri.ears.problems.unconstrained.cec2017;

public class F5 extends CEC2017 {

    // Shifted and Rotated Schaffer F7 Function
    public F5(int d) {
        super("F05", d, 5);
    }

    @Override
    public double eval(double[] x) {
        return schafferF7Func(x, numberOfDimensions, 1, 1, 0) + 100.0 * funcNum;
    }
}