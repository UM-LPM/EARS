package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F30 extends CEC2014 {

    public F30(int d) {
        super(d, 30);

        name = "F30 Composition Function 8";
    }

    @Override
    public double eval(double[] x) {
        return Functions.cf08(x, numberOfDimensions, OShift, M, SS, 1) + funcNum * 100.0;
    }
}