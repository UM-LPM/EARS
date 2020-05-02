package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;
import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_M.html#go_benchmark.Mishra04
different equation
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/149-mishra-s-function-no-4
 */
public class Mishra4 extends Problem {

    public Mishra4() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Mishra4";

        optimum[0] = new double[]{-9.941127263635860, -9.999571661999983};
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(abs(sin(sqrt(abs(pow(x[0], 2) + x[1])))), 0.5) + (x[0] + x[1]) / 100.0;
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -0.199406970088833;
    }
}