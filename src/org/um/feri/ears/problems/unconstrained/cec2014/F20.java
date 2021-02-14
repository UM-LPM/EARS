package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F20 extends CEC2014 {

    public F20(int d) {
        super(d, 20);

        name = "F20 Hybrid Function 4";
    }

    @Override
    public double eval(double[] x) {
        return Functions.hf04(x, numberOfDimensions, OShift, M, SS, 1, 1) + funcNum * 100.0;
    }
}