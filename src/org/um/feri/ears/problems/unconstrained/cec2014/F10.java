package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F10 extends CEC2014 {

    public F10(int d) {
        super("F10 Schwefel function", d, 10);
    }

    @Override
    public double eval(double[] x) {
        return Functions.schwefel_func(x, numberOfDimensions, OShift, M, 1, 0) + funcNum * 100.0;
    }
}
