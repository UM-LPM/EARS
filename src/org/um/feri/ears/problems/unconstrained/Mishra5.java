package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/150-mishra-s-function-no-5
http://infinity77.net/global_optimization/test_functions_nd_M.html#go_benchmark.Mishra05
 */
public class Mishra5 extends Problem {

    public Mishra5() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Mishra5";

        optimum[0] = new double[]{-1.986820662153768, -10};
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(pow(sin(pow(cos(x[0]) + cos(x[1]), 2)), 2) + pow(cos(pow(sin(x[0]) + sin(x[1]), 2)), 2) + x[0], 2) + 0.01 * (x[0] + 10 * x[1]);
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -1.019829519930943;
    }
}