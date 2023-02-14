package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.SineEnvelope
 */
public class SineEnvelope extends DoubleProblem {

    public SineEnvelope() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "SineEnvelope";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double numerator, denominator;
        for (int i = 0; i < numberOfDimensions - 1; i++) {
            numerator = pow(sin(sqrt(pow(x[i + 1], 2) + pow(x[i], 2)) - 0.5), 2);
            denominator = pow(0.001 * (pow(x[i + 1], 2) + pow(x[i], 2)) + 1.0, 2);
            fitness += numerator / denominator + 0.5;
        }
        return -fitness;
    }
}