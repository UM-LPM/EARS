package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

/*
https://www.sfu.ca/~ssurjano/mccorm.html
http://benchmarkfcns.xyz/benchmarkfcns/mccormickfcn.html
 */

public class McCormick extends DoubleProblem {

    public McCormick() {
        super("McCormick", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));

        lowerLimit.set(0, -1.5);
        upperLimit.set(0, 4.0);

        lowerLimit.set(1, -3.0);
        upperLimit.set(1, 4.0);

        decisionSpaceOptima[0][0] = -0.54719;
        decisionSpaceOptima[0][1] = -1.54719;

        objectiveSpaceOptima[0] = -1.913222954882274;
    }

    @Override
    public double eval(double[] x) {
        return sin(x[0] + x[1]) + pow(x[0] - x[1], 2) + -1.5 * x[0] + 2.5 * x[1] + 1;
    }
}