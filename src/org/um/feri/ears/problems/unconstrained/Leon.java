package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://al-roomi.org/benchmarks/unconstrained/2-dimensions/125-leon-s-function
http://benchmarkfcns.xyz/benchmarkfcns/leonfcn.html
 */

public class Leon extends DoubleProblem {

    public Leon() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -1.2));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.2));
        name = "Leon";

        Arrays.fill(decisionSpaceOptima[0], 1);
    }

    @Override
    public double eval(double[] x) {
        return 100.0 * pow(x[1] - pow(x[0], 3), 2) + pow(1 - x[0], 2);
    }
}