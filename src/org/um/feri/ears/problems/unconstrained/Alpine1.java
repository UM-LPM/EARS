package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.sin;
/*
http://infinity77.net/global_optimization/test_functions_nd_A.html#go_benchmark.Alpine01
http://benchmarkfcns.xyz/benchmarkfcns/alpinen1fcn.html
 */

public class Alpine1 extends DoubleProblem {

    public Alpine1() {
        super("Alpine1", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += abs(x[i] * sin(x[i]) + 0.1 * x[i]);
        }
        return fitness;
    }
}