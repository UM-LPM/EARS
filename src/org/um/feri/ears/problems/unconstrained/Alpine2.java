package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://benchmarkfcns.xyz/benchmarkfcns/alpinen2fcn.html
http://infinity77.net/global_optimization/test_functions_nd_A.html#go_benchmark.Alpine02
 */
public class Alpine2 extends Problem {

    public Alpine2() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Alpine2";

        Arrays.fill(optimum[0], 7.917);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 1.0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness *= sqrt(x[i]) * sin(x[i]);
        }
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return Math.pow(2.808, numberOfDimensions);
    }
}