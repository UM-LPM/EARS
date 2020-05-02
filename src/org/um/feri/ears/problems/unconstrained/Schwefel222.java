package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schwefel22
http://benchmarkfcns.xyz/benchmarkfcns/schwefel222fcn.html
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/190-schwefel-s-function-2-22
 */
public class Schwefel222 extends Problem {
    public Schwefel222(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Schwefel222";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum = 0, prod = 1.0;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum = sum + abs(x[i]);
            prod = prod * abs(x[i]);
        }
        fitness = sum + prod;
        return fitness;
    }
}
