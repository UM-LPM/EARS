package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;


public class F6 extends CEC2015 {

    public F6(int d) {
        super(d, 6);

        name = "F06 HappyCat Function";
    }

    @Override
    public double eval(double[] x) {
        return Functions.happycat_func(x, numberOfDimensions, OShift, M, 1, 1) + 100.0 * funcNum;
    }
}
