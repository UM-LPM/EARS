package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F14 extends CEC2014 {

    public F14(int d) {
        super("F14 HGBat function", d, 14);
    }

    @Override
    public double eval(double[] x) {
        return Functions.hgbat_func(x, numberOfDimensions, OShift, M, 1, 1) + funcNum * 100.0;
    }
}