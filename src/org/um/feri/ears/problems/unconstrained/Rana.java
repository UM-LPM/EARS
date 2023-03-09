package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_R.html#go_benchmark.Rana
 */
public class Rana extends DoubleProblem {

    public Rana() {
        super("Rana", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -500.000001));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 500.000001));

        Arrays.fill(decisionSpaceOptima[0], -500.0);
        objectiveSpaceOptima[0] = -928.5478554047827;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += x[i] * sin(sqrt(abs(x[0] - x[i] + 1))) * cos(sqrt(abs(x[0] + x[i] + 1))) + (x[0] + 1) * sin(sqrt(abs(x[0] + x[i] + 1))) * cos(sqrt(abs(x[0] - x[i] + 1)));
        }
        return fitness;
    }
}