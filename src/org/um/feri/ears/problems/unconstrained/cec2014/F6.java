package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F6 extends CEC2014 {

    public F6(int d) {
        super("F06 Weierstrass Function", d, 6);
    }

    @Override
    public double eval(double[] x) {
        return Functions.weierstrass_func(x, numberOfDimensions, OShift, M, 1, 1) + funcNum * 100.0;
    }
}
