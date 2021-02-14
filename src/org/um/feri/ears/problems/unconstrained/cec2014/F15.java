package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F15 extends CEC2014 {

    public F15(int d) {
        super(d, 15);

        name = "F15 Griewank-Rosenbrock function";
    }

    @Override
    public double eval(double[] x) {
        return Functions.grie_rosen_func(x, numberOfDimensions, OShift, M, 1, 1) + funcNum * 100.0;
    }
}