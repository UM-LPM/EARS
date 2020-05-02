package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
/*
http://infinity77.net/global_optimization/test_functions_nd_M.html#go_benchmark.Mishra07
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/152-mishra-s-function-no-10a-or-seqp-function-no-1
 */
public class Mishra10 extends Problem {

    public Mishra10() {
        super(2, 0,2);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Mishra10";

        optimum[0] = new double[]{0.0, 0.0};
        optimum[1] = new double[]{2.0, 2.0};
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow((x[0] + x[1]) - (x[0] * x[1]), 2);
        return fitness;
    }
}