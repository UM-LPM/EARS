package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F26 extends CEC2014 {

    public F26(int d) {
        super("F26 Composition Function 4", d, 26);
    }

    @Override
    public double eval(double[] x) {
        return Functions.cf04(x, numberOfDimensions, OShift, M, 1) + funcNum * 100.0;
    }
}