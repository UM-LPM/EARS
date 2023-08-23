package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.sfu.ca/~ssurjano/sumsqu.html
http://benchmarkfcns.xyz/benchmarkfcns/sumsquaresfcn.html
 */

public class SumOfSquares extends DoubleProblem {
    public SumOfSquares(int d) {
        super("SumOfSquares", d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
    }

    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += (i + 1) * pow(x[i], 2);
        }
        return fitness;
    }
}
