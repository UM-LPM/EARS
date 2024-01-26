package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F13 extends CEC2015 {
    // Composition Function 1
    public F13(int d) {
        super("F13", d, 13);
    }

    @Override
    public double eval(double[] x) {
        return Functions.cf01(x, numberOfDimensions, OShift, M, 1) + 100.0 * funcNum;
    }
}
