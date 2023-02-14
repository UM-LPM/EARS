package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

/*
http://infinity77.net/global_optimization/test_functions_nd_T.html#go_benchmark.Trigonometric02
 */

public class Trigonometric2 extends DoubleProblem {

    public Trigonometric2() {
        super("Trigonometric2", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 500.0));
        Arrays.fill(decisionSpaceOptima[0], 0.9);
        objectiveSpaceOptima[0] = 1.0;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += 8 * pow(sin(7 * pow(x[i] - 0.9, 2)), 2)
                    + 6 * pow(sin(14 * pow(x[0] - 0.9, 2)), 2)
                    + pow(x[i] - 0.9, 2);
        }
        return 1 + fitness;
    }
}