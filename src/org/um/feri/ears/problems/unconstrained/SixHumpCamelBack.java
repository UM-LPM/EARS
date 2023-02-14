package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.SixHumpCamel
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/23-six-hump-camel-back-function
 */

public class SixHumpCamelBack extends DoubleProblem {
    public SixHumpCamelBack() {
        super(2, 2, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));
        // -3 <= x1 <= 3
        // -2 <= x1 <= 2
        name = "SixHumpCamelBack";

        decisionSpaceOptima[0][0] = -0.08984201368301331;
        decisionSpaceOptima[0][1] = 0.7126564032704135;

        decisionSpaceOptima[1][0] = 0.08984201368301331;
        decisionSpaceOptima[1][1] = -0.7126564032704135;
        objectiveSpaceOptima[0] = -1.031628453489877;
    }

    @Override
    public double eval(double[] x) {
        return 4 * pow(x[0], 2)
                - 2.1 * pow(x[0], 4)
                + (1.0 / 3.0) * pow(x[0], 6)
                + x[0] * x[1]
                - 4 * pow(x[1], 2)
                + 4 * pow(x[1], 4);
    }
}
