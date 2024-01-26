package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F3 extends CEC2015 {
    // Weierstrass's Function
    public F3(int d) {
        super("F03", d, 3);
    }

    @Override
    public double eval(double[] x) {
        return Functions.weierstrass_func(x, numberOfDimensions, OShift, M, 1, 1) + 100.0 * funcNum;
    }
}
