package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F25 extends CEC2014 {

    public F25(int d) {
        super("F25 Composition Function 3", d, 25);
    }

    @Override
    public double eval(double[] x) {
        return Functions.cf03(x, numberOfDimensions, OShift, M, 1) + funcNum * 100.0;
    }
}