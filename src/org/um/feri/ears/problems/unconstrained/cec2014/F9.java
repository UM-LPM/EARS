package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F9 extends CEC2014 {

    public F9(int d) {
        super(d, 9);

        name = "F09 Rastrigin Function";
    }

    @Override
    public double eval(double[] x) {
        return Functions.rastrigin_func(x, numberOfDimensions, OShift, M, 1, 1) + funcNum * 100.0;
    }
}
