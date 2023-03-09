package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Price02
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/158-price-s-function-no-2
http://benchmarkfcns.xyz/benchmarkfcns/periodicfcn.html
 */
public class Price2 extends DoubleProblem {

    public Price2() {
        super("Price2", 2, 1, 1, 0);
        // also known as Periodic Function
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        objectiveSpaceOptima[0] = 0.9;
    }

    @Override
    public double eval(double[] x) {
        return 1 + pow(sin(x[0]), 2) + pow(sin(x[1]), 2) - 0.1 * exp(-pow(x[0], 2) - pow(x[1], 2));
    }
}