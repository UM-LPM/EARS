package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F1 extends CEC2015 {


    public F1(int d) {
        super("F01 Bent Cigar", d, 1);
    }

    @Override
    public double eval(double[] x) {
        return Functions.bent_cigar_func(x, numberOfDimensions, OShift, M, 1, 1) + 100.0 * funcNum;
    }
}
