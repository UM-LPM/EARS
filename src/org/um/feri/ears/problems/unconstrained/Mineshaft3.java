package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/115-mineshaft-function-no-3
 */
public class Mineshaft3 extends DoubleProblem {

    public Mineshaft3() {
        super("Mineshaft3", 2, 1, 1, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.0));

        decisionSpaceOptima[0] = new double[]{0.8, 1.3};
        objectiveSpaceOptima[0] = -7.0;
    }

    @Override
    public double eval(double[] x) {
        return -5 * exp(-1000 * pow(x[0] - 0.5, 2) - 1000 * pow(x[1] - 0.3, 2))
                - 7 * exp(-2000 * pow(x[0] - 0.8, 2) - 2000 * pow(x[1] - 1.3, 2));
    }
}