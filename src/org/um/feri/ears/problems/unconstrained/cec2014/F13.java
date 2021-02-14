package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F13 extends CEC2014 {

    public F13(int d) {
        super(d, 13);

        name = "F13 HappyCat function";
    }

    @Override
    public double eval(double[] x) {
        return Functions.happycat_func(x, numberOfDimensions, OShift, M, 1, 1) + funcNum * 100.0;
    }
}
