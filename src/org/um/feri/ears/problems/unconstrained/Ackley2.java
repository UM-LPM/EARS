package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://benchmarkfcns.xyz/benchmarkfcns/ackleyn2fcn.html
 */
public class Ackley2 extends Problem {

    public Ackley2() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -32.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 32.0));
        name = "Ackley2";
    }

    @Override
    public double eval(double[] x) {
        return -200.0 * exp(-0.02 * sqrt(pow(x[0], 2) + pow(x[1], 2)));
    }

    @Override
    public double getGlobalOptimum() {
        return -200.0;
    }
}