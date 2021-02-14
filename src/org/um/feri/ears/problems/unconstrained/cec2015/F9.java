package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F9 extends CEC2015 {

    public F9(int d) {
        super(d, 9);

        name = "F09 Expanded Scaffer's Function";
    }

    @Override
    public double eval(double[] x) {
        return Functions.escaffer6_func(x, numberOfDimensions, OShift, M, 1, 1) + 100.0 * funcNum;
    }
}
