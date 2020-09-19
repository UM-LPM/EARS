package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_T.html#go_benchmark.Trefethen
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/105-trefethen-s-function
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/105-trefethen-s-function
 */

public class Trefethen extends Problem {

    public Trefethen() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Trefethen";

        optimum[0] = new double[]{-0.02440307923, 0.2106124261};
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0.25 * pow(x[0], 2) + 0.25 * pow(x[1], 2) + exp(sin(50.0 * x[0])) - sin(10.0 * x[0] +
                10.0 * x[1]) + sin(60.0 * exp(x[1])) + sin(70.0 * sin(x[0])) + sin(sin(80.0 * x[1]));
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -3.3068686474752305;
    }
}