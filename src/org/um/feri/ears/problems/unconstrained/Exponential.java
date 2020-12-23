package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_E.html#go_benchmark.Exponential
http://benchmarkfcns.xyz/benchmarkfcns/exponentialfcn.html
 */
public class Exponential extends Problem {

    public Exponential() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.0));
        name = "Exponential";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(x[i], 2);
        }
        return -exp(-0.5 * fitness);
    }

    @Override
    public double getGlobalOptimum() {
        return -1.0;
    }
}