package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.sfu.ca/~ssurjano/beale.html
http://benchmarkfcns.xyz/benchmarkfcns/bealefcn.html
*/

public class Beale extends DoubleProblem {

    public Beale() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -4.5));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 4.5));
        name = "Beale";

        decisionSpaceOptima[0][0] = 3;
        decisionSpaceOptima[0][1] = 0.5;
    }

    @Override
    public double eval(double[] x) {
        return pow(1.5 - x[0] + x[0] * x[1], 2)
                + pow(2.250 - x[0] + x[0] * x[1] * x[1], 2)
                + pow(2.625 - x[0] + x[0] * x[1] * x[1] * x[1], 2);
    }
}
