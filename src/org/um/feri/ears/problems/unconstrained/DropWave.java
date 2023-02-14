package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/drop.html
http://benchmarkfcns.xyz/benchmarkfcns/dropwavefcn.html
 */
public class DropWave extends DoubleProblem {

    public DropWave() {
        super("DropWave", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.12));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.12));
        name = "DropWave";
        objectiveSpaceOptima[0] = -1.0;
    }

    @Override
    public double eval(double[] x) {
        return -((1.0 + cos(12.0 * sqrt(pow(x[0], 2) + pow(x[1], 2)))) / (((pow(x[0], 2) + pow(x[1], 2)) / 2.0) + 2.0));
    }
}
