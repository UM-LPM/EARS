package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F7 extends CEC2015 {

    public F7(int d) {
        super("F07 HGBat Function", d, 7);
    }

    @Override
    public double eval(double[] x) {
        return Functions.hgbat_func(x, numberOfDimensions, OShift, M, 1, 1) + 100.0 * funcNum;
    }
}
