package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
infinity77.net/global_optimization/test_functions_nd_L.html#go_benchmark.Levy05
 */

public class Levy5 extends Problem {

    public Levy5() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Levy5";

        optimum[0][0] = -1.3068;
        optimum[0][1] = -1.4248;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum1 = 0, sum2 = 0;

        for (int i = 1; i < 6; i++) {
            sum1 += i * cos((i - 1) * x[0] + i);
            sum2 += i * cos((i + 1) * x[1] + i);
        }
        fitness = sum1 * sum2 + pow(x[0] + 1.42513, 2) + pow(x[1] + 0.80032, 2);
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -176.13757123796194;
    }
}