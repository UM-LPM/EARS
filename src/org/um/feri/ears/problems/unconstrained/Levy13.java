package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/levy13.html
http://benchmarkfcns.xyz/benchmarkfcns/levin13fcn.html
 */

public class Levy13 extends DoubleProblem {

    public Levy13() {
        super("Levy13", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        Arrays.fill(decisionSpaceOptima[0], 1);
    }

    @Override
    public double eval(double[] x) {
        return pow(sin(3 * PI * x[0]), 2) + pow(x[0] - 1, 2) *
                (1 + pow(Math.sin(3 * PI * x[1]), 2)) + pow(x[1] - 1, 2) *
                (1 + pow(Math.sin(2 * PI * x[1]), 2));
    }
}