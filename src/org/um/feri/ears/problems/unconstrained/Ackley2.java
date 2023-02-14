package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://benchmarkfcns.xyz/benchmarkfcns/ackleyn2fcn.html
 */
public class Ackley2 extends DoubleProblem {

    public Ackley2() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -32.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 32.0));
        name = "Ackley2";
        objectiveSpaceOptima[0] = -200.0;
    }

    @Override
    public double eval(double[] x) {
        return -200.0 * exp(-0.02 * sqrt(pow(x[0], 2) + pow(x[1], 2)));
    }
}