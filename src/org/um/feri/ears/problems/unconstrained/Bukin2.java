package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_B.html#go_benchmark.Bukin06
wrong optimum given at link
 */

public class Bukin2 extends Problem {

    public Bukin2() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        name = "Bukin2";

        lowerLimit.set(0, -15.0);
        upperLimit.set(0, -5.0);

        lowerLimit.set(1, -3.0);
        upperLimit.set(1, 3.0);

        optimum[0][0] = -15;
        optimum[0][1] = 0.0;
    }

    @Override
    public double eval(double[] x) {
        return 100.0 * (pow(x[1], 2) - 0.01 * pow(x[0], 2) + 1.0) + 0.01 * pow(x[0] + 10, 2);
    }

    @Override
    public double getGlobalOptimum() {
        return -124.75;
    }
}