package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.PenHolder
 */

public class PenHolder extends DoubleProblem {

    public PenHolder() {
        super("Pen Holder", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -11.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 11.0));

        Arrays.fill(decisionSpaceOptima[0], 9.646167671043401);
        objectiveSpaceOptima[0] = -0.9635348327265058;
    }

    @Override
    public double eval(double[] x) {
        return -exp(-1 / abs(cos(x[0]) * cos(x[1]) * exp(abs(1 - sqrt(pow(x[0], 2) + pow(x[1], 2)) / PI))));
    }
}