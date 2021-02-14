package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F8 extends CEC2014 {

    public F8(int d) {
        super(d, 8);

        name = "F08 Rastrigin Function";
    }

    @Override
    public double eval(double[] x) {
        return Functions.rastrigin_func(x, numberOfDimensions, OShift, M, 1, 0) + funcNum * 100.0;
    }
}