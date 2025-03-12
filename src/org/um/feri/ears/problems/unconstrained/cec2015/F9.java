package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F9 extends CEC2015 {
    // Expanded Scaffer's Function
    public F9(int d) {
        super("F09", d, 9);
    }

    @Override
    public double eval(double[] x) {
        return Functions.escaffer6_func(x, numberOfDimensions, oShift, M, 1, 1) + 100.0 * funcNum;
    }
}
