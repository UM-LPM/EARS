package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F11 extends CEC2014 {

    public F11(int d) {
        super("F11 Schwefel function", d, 11);
    }

    @Override
    public double eval(double[] x) {
        return Functions.schwefel_func(x, numberOfDimensions, OShift, M, 1, 1) + funcNum * 100.0;
    }
}
