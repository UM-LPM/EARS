package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.cos;
import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_V.html#go_benchmark.VenterSobiezcczanskiSobieski
 */
public class VenterSobiezcczanskiSobieski extends Problem {

    public VenterSobiezcczanskiSobieski() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -50.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 50.0));
        name = "VenterSobiezcczanskiSobieski";
    }

    @Override
    public double eval(double[] x) {
        double u = pow(x[0], 2.0) - 100.0 * pow(cos(x[0]), 2.0);
        double v = -100.0 * cos(pow(x[0], 2.0) / 30.0) + pow(x[1], 2.0);
        double w = -100.0 * pow(cos(x[1]), 2.0) - 100.0 * cos(pow(x[1], 2.0) / 30.0);
        return u + v + w;
    }

    @Override
    public double getGlobalOptimum() {
        return -400.0;
    }
}