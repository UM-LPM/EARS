package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_B.html#go_benchmark.Brent
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/112-brent-s-function
http://benchmarkfcns.xyz/benchmarkfcns/brentfcn.html
 */
public class Brent extends Problem {

    public Brent() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Brent";

        optimum[0] = new double[]{-10.0, -10.0};
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0] + 10, 2) + pow(x[1] + 10, 2) + exp(-pow(x[0], 2) - pow(x[1], 2));
    }
}