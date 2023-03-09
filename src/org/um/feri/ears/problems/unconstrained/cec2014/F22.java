package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F22 extends CEC2014 {

    public F22(int d) {
        super("F22 Hybrid Function 6", d, 22);
    }

    @Override
    public double eval(double[] x) {
        return Functions.hf06(x, numberOfDimensions, OShift, M, SS, 1, 1) + funcNum * 100.0;
    }
}