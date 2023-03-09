package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

/*
http://infinity77.net/global_optimization/test_functions_nd_G.html#go_benchmark.Giunta
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/154-giunta-s-function
 */

public class Giunta extends DoubleProblem {

    public Giunta() {
        super("Giunta", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.0));

        decisionSpaceOptima[0] = new double[]{0.4673200277395354, 0.4673200169591304};
        objectiveSpaceOptima[0] = 0.06447042053690566;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += sin(16.0 / 15.0 * x[i] - 1)
                    + pow(sin(16.0 / 15.0 * x[i] - 1), 2)
                    + (1.0 / 50.0) * sin(4 * (16.0 / 15.0 * x[i] - 1));
        }
        return 0.6 + fitness;
    }
}