package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F21 extends CEC2014 {

    public F21(int d) {
        super("F21 Hybrid Function 5", d, 21);
    }

    @Override
    public double eval(double[] x) {
        return Functions.hf05(x, numberOfDimensions, OShift, M, SS, 1, 1) + funcNum * 100.0;
    }
}