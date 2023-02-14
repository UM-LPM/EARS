package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schwefel36
 */
public class Schwefel236 extends DoubleProblem {

    public Schwefel236() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "Schwefel236";

        Arrays.fill(decisionSpaceOptima[0], 12.0);
        objectiveSpaceOptima[0] = -3456;
    }

    @Override
    public double eval(double[] x) {
        return -x[0] * x[1] * (72 - 2 * x[0] - 2 * x[1]);
    }
}