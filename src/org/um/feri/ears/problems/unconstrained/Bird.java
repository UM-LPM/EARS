package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/29-bird-function
http://infinity77.net/global_optimization/test_functions_nd_B.html#go_benchmark.Bird
 */
public class Bird extends DoubleProblem {

    public Bird() {
        super("Bird", 2, 2, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -2.0 * Math.PI));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 2.0 * Math.PI));

        decisionSpaceOptima[0] = new double[]{4.701055751981055, 3.152946019601391};
        decisionSpaceOptima[1] = new double[]{-1.582142172055011, -3.130246799635430};
        objectiveSpaceOptima[0] = -106.7645367198034;
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0] - x[1], 2) + exp(pow(1 - sin(x[0]), 2)) * cos(x[1]) + exp(pow(1 - cos(x[1]), 2)) * sin(x[0]);
    }
}