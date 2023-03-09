package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F5 extends CEC2014 {

    public F5(int d) {
        super("F05 Ackley Function", d, 5);
    }

    @Override
    public double eval(double[] x) {
        return Functions.ackley_func(x, numberOfDimensions, OShift, M, 1, 1) + funcNum * 100.0;
    }
}
