package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.cos;
import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_W.html#go_benchmark.Whitley
 */
public class Whitley extends DoubleProblem {

    public Whitley() {
        super("Whitley", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.24));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.24));

        Arrays.fill(decisionSpaceOptima[0], 1.0);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            for (int j = 0; j < numberOfDimensions; j++) {
                fitness += pow(100.0 * pow(pow(x[i], 2) - x[j], 2) + pow(1 - x[j], 2), 2) / 4000.0
                        - cos(100.0 * pow(pow(x[i], 2) - x[j], 2) + pow(1 - x[j], 2)) + 1;
            }
        }
        return fitness;
    }
}