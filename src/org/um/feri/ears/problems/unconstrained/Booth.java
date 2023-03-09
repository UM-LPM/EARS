package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.sfu.ca/~ssurjano/booth.html
http://benchmarkfcns.xyz/benchmarkfcns/boothfcn.html
*/

public class Booth extends DoubleProblem {

    public Booth() {
        super("Booth", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        decisionSpaceOptima[0][0] = 1.0;
        decisionSpaceOptima[0][1] = 3.0;
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0] + 2 * x[1] - 7, 2) + pow(2 * x[0] + x[1] - 5, 2);
    }
}
