package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/156-quadratic-function
http://infinity77.net/global_optimization/test_functions_nd_Q.html#go_benchmark.Quadratic
http://benchmarkfcns.xyz/benchmarkfcns/quarticfcn.html
 */
public class Quadratic extends Problem {

    public Quadratic() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Quadratic";

        optimum[0] = new double[]{0.193880169366971, 0.485133920218833};
    }

    @Override
    public double eval(double[] x) {
        return -3803.84 - 138.08 * x[0] - 232.92 * x[1] + 128.08 * pow(x[0], 2) + 203.64 * pow(x[1], 2) + 182.25 * x[0] * x[1];
    }

    @Override
    public double getGlobalOptimum() {
        return -3873.724182186271819;
    }
}