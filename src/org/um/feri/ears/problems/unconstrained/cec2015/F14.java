package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F14 extends CEC2015 {

    public F14(int d) {
        super("F14 Composition Function 2", d, 14);
    }

    @Override
    public double eval(double[] x) {
        return Functions.cf03(x, numberOfDimensions, OShift, M, 1) + 100.0 * funcNum;
    }
}
