package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/sumsqu.html
http://benchmarkfcns.xyz/benchmarkfcns/sumsquaresfcn.html
 */

public class SumSquares extends Problem {
    public SumSquares(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "SumSquares";
    }

    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += (i + 1) * pow(x[i], 2);
        }
        return fitness;
    }
}
