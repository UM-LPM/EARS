package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_Z.html#go_benchmark.ZeroSum
 */
public class ZeroSum extends DoubleProblem {

    public ZeroSum() {
        super("ZeroSum", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum = 0;
        for (int i = 0; i < numberOfDimensions - 1; i++) {
            sum += x[i];
        }
        if (sum == 0)
            fitness = 0.0;
        else
            fitness = 1 + pow(10000.0 * abs(sum), 0.5);

        return fitness;
    }
}