package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/111-chen-s-bird-function
 */
public class ChensBird extends Problem {

    double b = 0.001;

    public ChensBird() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "ChensBird";

        minimize = false;
        Arrays.fill(optimum[0], 0.5);
    }

    @Override
    public double eval(double[] x) {
        return (b / (pow(b, 2) + pow(pow(x[0], 2) + pow(x[1], 2) - 1, 2)))
                + (b / (pow(b, 2) + pow(pow(x[0], 2) + pow(x[1], 2) - 0.5, 2)))
                + (b / (pow(b, 2) + pow(x[0] - x[1], 2)));
    }

    @Override
    public double getGlobalOptimum() {
        return 2000.0039999840003;
    }

    @Override
    public boolean isMinimize() {
        return false;
    }
}