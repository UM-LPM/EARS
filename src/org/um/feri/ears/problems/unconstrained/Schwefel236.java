package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schwefel36
 */
public class Schwefel236 extends Problem {

    public Schwefel236() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "Schwefel236";

        Arrays.fill(optimum[0], 12.0);
    }

    @Override
    public double eval(double[] x) {
        double fitness = -x[0] * x[1] * (72 - 2 * x[0] - 2 * x[1]);
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -3456;
    }
}