package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F10 extends CEC2015 {

    public F10(int d) {
        super("F10 Hybrid Function 1", d, 10);
    }

    @Override
    public double eval(double[] x) {
        return Functions.hf01(x, numberOfDimensions, OShift, M, SS, 1, 1) + 100.0 * funcNum;
    }
}
