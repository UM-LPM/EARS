package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F1 extends CEC2015 {


    public F1(int d) {
        super(d, 1);

        name = "F01 Bent Cigar";
    }

    @Override
    public double eval(double[] x) {
        return Functions.bent_cigar_func(x, numberOfDimensions, OShift, M, 1, 1) + 100.0 * funcNum;
    }
}
