package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/56-himmelblau-s-function
http://infinity77.net/global_optimization/test_functions_nd_H.html#go_benchmark.HimmelBlau
http://benchmarkfcns.xyz/benchmarkfcns/himmelblaufcn.html
 */
public class HimmelBlau extends Problem {

    public HimmelBlau() {
        super(2, 0, 4);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -6.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 6.0));
        name = "HimmelBlau";

        optimum[0] = new double[]{3.0, 2.0};
        optimum[1] = new double[]{3.584428340330, -1.848126526964};
        optimum[2] = new double[]{-3.779310253378, -3.283185991286};
        optimum[3] = new double[]{-2.805118086953, 3.131312518250};
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0] + pow(x[1], 2) - 7, 2) + pow(pow(x[0], 2) + x[1] - 11, 2);
    }
}