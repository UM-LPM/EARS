package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F12 extends CEC2015 {

    public F12(int d) {
        super("F12 Hybrid Function 3", d, 12);
    }

    @Override
    public double eval(double[] x) {
        return Functions.hf06(x, numberOfDimensions, OShift, M, SS, 1, 1) + 100.0 * funcNum;
    }
}
