package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Price02
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/158-price-s-function-no-2
http://benchmarkfcns.xyz/benchmarkfcns/periodicfcn.html
 */
public class Price2 extends Problem {

    public Price2() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Price2"; // also known as Periodic Function
    }

    @Override
    public double eval(double[] x) {
        return 1 + pow(sin(x[0]), 2) + pow(sin(x[1]), 2) - 0.1 * exp(-pow(x[0], 2) - pow(x[1], 2));
    }

    @Override
    public double getGlobalOptimum() {
        return 0.9;
    }
}