package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.log;
import static java.lang.Math.sin;

/*
http://infinity77.net/global_optimization/test_functions_nd_V.html#go_benchmark.Vincent
 */
public class Vincent extends DoubleProblem {

    public Vincent() {
        super("Vincent", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.25));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        Arrays.fill(decisionSpaceOptima[0], 7.70628098);
        objectiveSpaceOptima[0] = -numberOfDimensions;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += sin(10 * log(x[i]));
        }
        return -fitness;
    }
}