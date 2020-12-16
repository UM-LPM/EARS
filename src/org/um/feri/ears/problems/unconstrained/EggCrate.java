package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
/*
http://infinity77.net/global_optimization/test_functions_nd_E.html#go_benchmark.EggCrate
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/122-egg-crate-function
http://benchmarkfcns.xyz/benchmarkfcns/eggcratefcn.html
 */
public class EggCrate extends Problem {

    public EggCrate() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
        name = "EggCrate";
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0], 2) + pow(x[1], 2) + 25 * (pow(sin(x[0]), 2) + pow(sin(x[1]), 2));
    }
}