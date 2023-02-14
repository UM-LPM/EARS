package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/134-jennrich-sampson-s-function
http://infinity77.net/global_optimization/test_functions_nd_J.html#go_benchmark.JennrichSampson
 */
public class JennrichSampson extends DoubleProblem {

    public JennrichSampson() {
        super("JennrichSampson", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.0));

        decisionSpaceOptima[0] = new double[]{0.25782521321500883, 0.25782521381356827};
        objectiveSpaceOptima[0] = 124.36218235561473896;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int k = 1; k <= 10; k++) {
            fitness += pow(2 + 2 * k - (exp(k * x[0]) + exp(k * x[1])), 2);
        }
        return fitness;
    }
}