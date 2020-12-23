package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.atan;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/118-s3-function
 */
public class S3 extends Problem {

    public S3() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "S3";

        optimum[0] = new double[]{10.0, 0.7};
    }

    @Override
    public double eval(double[] x) {
        return 2 + pow(x[1] - 0.7, 2) - atan(x[0]);
    }

    @Override
    public double getGlobalOptimum() {
        return 0.528872325696265;
    }
}