package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F3 extends CEC2015 {

    public F3(int d) {
        super(d, 3);

        name = "F03 Weierstrass's Function";
    }

    @Override
    public double eval(double[] x) {
        return Functions.weierstrass_func(x, numberOfDimensions, OShift, M, 1, 1) + 100.0 * funcNum;
    }
}
