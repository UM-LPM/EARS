package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.SixHumpCamel
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/23-six-hump-camel-back-function
 */

public class SixHumpCamelBack extends Problem {
    public SixHumpCamelBack() {
        super(2, 0, 2);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
        // -3 <= x1 <= 3
        // -2 <= x1 <= 2
        name = "SixHumpCamelBack";

        optimum[0][0] = -0.08984201368301331;
        optimum[0][1] = 0.7126564032704135;

        optimum[1][0] = 0.08984201368301331;
        optimum[1][1] = -0.7126564032704135;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        fitness = 4 * pow(x[0], 2)
                - 2.1 * pow(x[0], 4)
                + (1.0 / 3.0) * pow(x[0], 6)
                + x[0] * x[1]
                - 4 * pow(x[1], 2)
                + 4 * pow(x[1], 4);
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -1.031628453489877;
    }
}
