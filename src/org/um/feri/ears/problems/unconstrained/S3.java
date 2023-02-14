package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.atan;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/118-s3-function
 */
public class S3 extends DoubleProblem {

    public S3() {
        super("S3", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        decisionSpaceOptima[0] = new double[]{10.0, 0.7};
        objectiveSpaceOptima[0] = 0.528872325696265;
    }

    @Override
    public double eval(double[] x) {
        return 2 + pow(x[1] - 0.7, 2) - atan(x[0]);
    }
}