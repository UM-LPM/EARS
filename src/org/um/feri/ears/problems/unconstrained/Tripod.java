package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/71-tripod-function
http://infinity77.net/global_optimization/test_functions_nd_T.html#go_benchmark.Tripod
 */

public class Tripod extends DoubleProblem {

    public Tripod() {
        super("Tripod", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));

        decisionSpaceOptima[0] = new double[]{0.0, -50.0};
    }

    @Override
    public double eval(double[] x) {
        return p(x[1]) * (1 + p(x[0])) + abs(x[0] + 50 * p(x[1]) * (1 - 2 * p(x[0]))) + abs(x[1] + 50 * (1 - 2 * p(x[1])));
    }

    private int p(double x) {
        return (x >= 0) ? 1 : 0;
    }
}