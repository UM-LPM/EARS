package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.max;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schwefel06
 */
public class Schwefel26 extends Problem {

    public Schwefel26() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Schwefel26";

        optimum[0] = new double[]{1.0, 3.0};
    }

    @Override
    public double eval(double[] x) {
        return max(abs(x[0] + 2.0 * x[1] - 7.0), abs(2.0 * x[0] + x[1] - 5.0));
    }
}