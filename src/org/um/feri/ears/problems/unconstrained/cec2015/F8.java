package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F8 extends CEC2015 {
    // Griewank-Rosenbrock Function
    public F8(int d) {
        super("F08", d, 8);
    }

    @Override
    public double eval(double[] x) {
        return Functions.grie_rosen_func(x, numberOfDimensions, oShift, M, 1, 1) + 100.0 * funcNum;
    }
}
