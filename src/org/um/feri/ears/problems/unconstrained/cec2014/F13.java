package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F13 extends CEC2014 {

    public F13(int d) {
        super("F13 HappyCat function", d, 13);
    }

    @Override
    public double eval(double[] x) {
        return Functions.happycat_func(x, numberOfDimensions, OShift, M, 1, 1) + funcNum * 100.0;
    }
}
