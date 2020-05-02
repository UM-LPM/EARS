package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/48-banana-shape-function
 */
public class BananaShape extends Problem {

    public BananaShape() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        name = "BananaShape";

        lowerLimit.set(0, -1.5);
        upperLimit.set(0, 1.5);

        lowerLimit.set(1, -2.5);
        upperLimit.set(1, 0.5);
    }

    @Override
    public double eval(double[] x) {
        double fitness = -(100 / (10 * pow(pow(x[0] + 1, 2) - (x[1] + 1), 2) + pow(x[0], 2) + 4));
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -25.0;
    }
}