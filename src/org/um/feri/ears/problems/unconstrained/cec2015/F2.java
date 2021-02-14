package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F2 extends CEC2015 {

    public F2(int d) {
        super(d, 2);

        name = "F02 Discus Function";
    }

    @Override
    public double eval(double[] x) {
        return Functions.discus_func(x, numberOfDimensions, OShift, M, 1, 1) + 100.0 * funcNum;
    }
}
