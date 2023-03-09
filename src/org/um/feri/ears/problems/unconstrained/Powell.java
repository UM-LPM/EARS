package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Powell
 */

public class Powell extends DoubleProblem {

    public Powell(int d) {
        super("Powell", d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -4.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        int k = 4;
        for (int i = 0; i < numberOfDimensions / k; i++) {
            fitness +=
                    +pow(x[4 * (i + 1) - 3 - 1] + 10 * x[4 * (i + 1) - 2 - 1], 2)
                            + 5 * pow(x[4 * (i + 1) - 1 - 1] - x[4 * (i + 1) - 1], 2)
                            + pow(x[4 * (i + 1) - 2 - 1] - x[4 * (i + 1) - 1 - 1], 4)
                            + pow(x[4 * (i + 1) - 3 - 1] - x[4 * (i + 1) - 1], 4);
        }
        return fitness;
    }
}
