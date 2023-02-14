package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F17 extends CEC2014 {

    public F17(int d) {
        super("F17 Hybrid Function 1", d, 17);
    }

    @Override
    public double eval(double[] x) {
        return Functions.hf01(x, numberOfDimensions, OShift, M, SS, 1, 1) + funcNum * 100.0;
    }
}