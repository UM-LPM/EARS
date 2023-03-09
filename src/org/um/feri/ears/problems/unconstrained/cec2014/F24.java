package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F24 extends CEC2014 {

    public F24(int d) {
        super("F24 Composition Function 2", d, 24);
    }

    @Override
    public double eval(double[] x) {
        return Functions.cf02(x, numberOfDimensions, OShift, M, 1) + funcNum * 100.0;
    }
}