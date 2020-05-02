package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
/*
http://infinity77.net/global_optimization/test_functions_nd_A.html#go_benchmark.Alpine01
http://benchmarkfcns.xyz/benchmarkfcns/alpinen1fcn.html
 */

public class Alpine1 extends Problem {

    public Alpine1() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Alpine1";
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