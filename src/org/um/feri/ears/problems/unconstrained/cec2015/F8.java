package org.um.feri.ears.problems.unconstrained.cec2015;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F8 extends CEC2015 {

    public F8(int d) {
        super(d, 8);

        name = "F08 Griewank-Rosenbrock Function";
    }

    @Override
    public double eval(double[] x) {
        return Functions.grie_rosen_func(x, numberOfDimensions, OShift, M, 1, 1) + 100.0 * funcNum;
    }
}
