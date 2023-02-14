package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.cos;
import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_V.html#go_benchmark.VenterSobiezcczanskiSobieski
 */
public class VenterSobiezcczanskiSobieski extends DoubleProblem {

    public VenterSobiezcczanskiSobieski() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -50.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 50.0));
        name = "VenterSobiezcczanskiSobieski";
        objectiveSpaceOptima[0] = -400.0;
    }

    @Override
    public double eval(double[] x) {
        double u = pow(x[0], 2.0) - 100.0 * pow(cos(x[0]), 2.0);
        double v = -100.0 * cos(pow(x[0], 2.0) / 30.0) + pow(x[1], 2.0);
        double w = -100.0 * pow(cos(x[1]), 2.0) - 100.0 * cos(pow(x[1], 2.0) / 30.0);
        return u + v + w;
    }
}