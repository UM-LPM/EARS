package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/56-himmelblau-s-function
http://infinity77.net/global_optimization/test_functions_nd_H.html#go_benchmark.HimmelBlau
http://benchmarkfcns.xyz/benchmarkfcns/himmelblaufcn.html
 */
public class HimmelBlau extends DoubleProblem {

    public HimmelBlau() {
        super(2, 4, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -6.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 6.0));
        name = "HimmelBlau";

        decisionSpaceOptima[0] = new double[]{3.0, 2.0};
        decisionSpaceOptima[1] = new double[]{3.584428340330, -1.848126526964};
        decisionSpaceOptima[2] = new double[]{-3.779310253378, -3.283185991286};
        decisionSpaceOptima[3] = new double[]{-2.805118086953, 3.131312518250};
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0] + pow(x[1], 2) - 7, 2) + pow(pow(x[0], 2) + x[1] - 11, 2);
    }
}