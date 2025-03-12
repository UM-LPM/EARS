package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F4 extends CEC2015 {
    // Schwefel's Function
    public F4(int d) {
        super("F04", d, 4);
    }

    @Override
    public double eval(double[] x) {
        return Functions.schwefel_func(x, numberOfDimensions, oShift, M, 1, 0) + 100.0 * funcNum;
    }
}
