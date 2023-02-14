package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_R.html#go_benchmark.Ripple01
 */
public class Ripple1 extends DoubleProblem {

    public Ripple1() {
        super("Ripple1", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.0));

        Arrays.fill(decisionSpaceOptima[0], 0.1);
        objectiveSpaceOptima[0] = -2.2;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {

            double u = -2.0 * log(2.0) * pow((x[i] - 0.1) / 0.8, 2);
            double v = pow(sin(5.0 * PI * x[i]), 6) + 0.1 * pow(cos(500.0 * PI * x[i]), 2);

            fitness += -exp(u) * v;
        }
        return fitness;
    }
}