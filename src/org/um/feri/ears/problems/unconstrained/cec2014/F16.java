package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F16 extends CEC2014 {

    public F16(int d) {
        super("F16 Expanded Scaffer's function", d, 16);
    }

    @Override
    public double eval(double[] x) {
        return Functions.escaffer6_func(x, numberOfDimensions, OShift, M, 1, 1) + funcNum * 100.0;
    }
}