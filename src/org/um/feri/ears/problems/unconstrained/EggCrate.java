package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

/*
http://infinity77.net/global_optimization/test_functions_nd_E.html#go_benchmark.EggCrate
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/122-egg-crate-function
http://benchmarkfcns.xyz/benchmarkfcns/eggcratefcn.html
 */
public class EggCrate extends DoubleProblem {

    public EggCrate() {
        super("EggCrate", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0], 2) + pow(x[1], 2) + 25 * (pow(sin(x[0]), 2) + pow(sin(x[1]), 2));
    }
}