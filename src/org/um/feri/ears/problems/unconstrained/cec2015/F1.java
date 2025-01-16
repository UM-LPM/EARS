package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F1 extends CEC2015 {

    // Bent Cigar
    public F1(int d) {
        super("F01", d, 1);
    }

    @Override
    public double eval(double[] x) {
        return Functions.bent_cigar_func(x, numberOfDimensions, oShift, M, 1, 1) + 100.0 * funcNum;
    }
}
