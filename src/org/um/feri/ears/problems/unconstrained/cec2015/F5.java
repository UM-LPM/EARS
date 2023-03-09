package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F5 extends CEC2015 {

    public F5(int d) {
        super("F05 Katsuura Function", d, 5);
    }

    @Override
    public double eval(double[] x) {
        return Functions.katsuura_func(x, numberOfDimensions, OShift, M, 1, 1) + 100.0 * funcNum;
    }
}
