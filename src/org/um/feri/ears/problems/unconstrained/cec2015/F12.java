package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F12 extends CEC2015 {
    // Hybrid Function 3
    public F12(int d) {
        super("F12", d, 12);
    }

    @Override
    public double eval(double[] x) {
        return Functions.hf06(x, numberOfDimensions, oShift, M, SS, 1, 1) + 100.0 * funcNum;
    }
}
