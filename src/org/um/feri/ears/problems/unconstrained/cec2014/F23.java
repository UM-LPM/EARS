package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F23 extends CEC2014 {

    public F23(int d) {
        super("F23 Composition Function 1", d, 23);
    }

    @Override
    public double eval(double[] x) {
        return Functions.cf01(x, numberOfDimensions, OShift, M, 1) + funcNum * 100.0;
    }
}