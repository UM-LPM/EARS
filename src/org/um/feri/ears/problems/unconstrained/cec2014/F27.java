package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F27 extends CEC2014 {

    public F27(int d) {
        super(d, 27);

        name = "F27 Composition Function 5";
    }

    @Override
    public double eval(double[] x) {
        return Functions.cf05(x, numberOfDimensions, OShift, M, 1) + funcNum * 100.0;
    }
}