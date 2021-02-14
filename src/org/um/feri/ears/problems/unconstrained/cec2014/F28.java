package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F28 extends CEC2014 {

    public F28(int d) {
        super(d, 28);

        name = "F28 Composition Function 6";
    }

    @Override
    public double eval(double[] x) {
        return Functions.cf06(x, numberOfDimensions, OShift, M, 1) + funcNum * 100.0;
    }
}