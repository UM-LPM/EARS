package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
Efficient and Robust Gradient Enhanced Kriging Emulators
 */
public class HerbieSmoothed extends Problem {

    public HerbieSmoothed() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.0));
        name = "HerbieSmoothed";

        optimum[0] = new double[]{2.0, 2.0};
    }

    @Override
    public double eval(double[] x) {
        double fitness = 1.0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness *= exp(-pow(x[i] - 1, 2)) + exp(-0.8 * pow(x[i] + 1, 2));
        }
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return 0.13588514776692626;
    }
}