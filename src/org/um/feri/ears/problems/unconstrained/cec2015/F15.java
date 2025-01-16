package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F15 extends CEC2015 {
    // Composition Function 3
    public F15(int d) {
        super("F15", d, 15);
    }

    @Override
    public double eval(double[] x) {
        return Functions.cf05(x, numberOfDimensions, oShift, M, 1) + 100.0 * funcNum;
    }
}
