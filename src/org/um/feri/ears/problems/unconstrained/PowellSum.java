package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/*
https://www.sfu.ca/~ssurjano/sumpow.html
http://benchmarkfcns.xyz/benchmarkfcns/powellsumfcn.html
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Sodp
 */

public class PowellSum extends DoubleProblem {

    public PowellSum(int d) {
        super("Powell Sum", d, 1, 1, 0);
        // also known as Sum Of Different Powers
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.0));
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(abs(x[i]), (i + 2));
        }
        return fitness;
    }
}