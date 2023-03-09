package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.cos;
import static java.lang.Math.pow;

/*
infinity77.net/global_optimization/test_functions_nd_L.html#go_benchmark.Levy05
 */

public class Levy5 extends DoubleProblem {

    public Levy5() {
        super("Levy5", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        decisionSpaceOptima[0][0] = -1.3068;
        decisionSpaceOptima[0][1] = -1.4248;
        objectiveSpaceOptima[0] = -176.13757123796194;
    }

    @Override
    public double eval(double[] x) {
        double sum1 = 0, sum2 = 0;

        for (int i = 1; i < 6; i++) {
            sum1 += i * cos((i - 1) * x[0] + i);
            sum2 += i * cos((i + 1) * x[1] + i);
        }
        return sum1 * sum2 + pow(x[0] + 1.42513, 2) + pow(x[1] + 0.80032, 2);
    }
}