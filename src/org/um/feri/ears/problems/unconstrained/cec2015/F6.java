package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;


public class F6 extends CEC2015 {
    // HappyCat Function
    public F6(int d) {
        super("F06", d, 6);
    }

    @Override
    public double eval(double[] x) {
        return Functions.happycat_func(x, numberOfDimensions, OShift, M, 1, 1) + 100.0 * funcNum;
    }
}
