package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Powell
 */

public class Powell extends Problem {

    public Powell(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -4.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
        name = "Powell";
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
