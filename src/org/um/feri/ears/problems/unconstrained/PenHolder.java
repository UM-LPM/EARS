package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.PenHolder
 */

public class PenHolder extends Problem {

    public PenHolder() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -11.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 11.0));
        name = "Pen Holder";

        Arrays.fill(optimum[0], 9.646167671043401);
    }

    @Override
    public double eval(double[] x) {
        double fitness = -exp(-1 / abs(cos(x[0]) * cos(x[1]) * exp(abs(1 - sqrt(pow(x[0], 2) + pow(x[1], 2)) / PI))));
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -0.9635348327265058;
    }
}