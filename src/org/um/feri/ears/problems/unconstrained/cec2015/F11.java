package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F11 extends CEC2015 {
    // Hybrid Function 2
    public F11(int d) {
        super("F11", d, 11);
    }

    @Override
    public double eval(double[] x) {
        return Functions.hf03(x, numberOfDimensions, OShift, M, SS, 1, 1) + 100.0 * funcNum;
    }
}
