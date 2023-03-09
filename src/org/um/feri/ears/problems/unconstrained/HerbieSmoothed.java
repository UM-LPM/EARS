package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
Efficient and Robust Gradient Enhanced Kriging Emulators
 */
public class HerbieSmoothed extends DoubleProblem {

    public HerbieSmoothed() {
        super("HerbieSmoothed", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 2.0));

        decisionSpaceOptima[0] = new double[]{2.0, 2.0};
        objectiveSpaceOptima[0] = 0.13588514776692626;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 1.0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness *= exp(-pow(x[i] - 1, 2)) + exp(-0.8 * pow(x[i] + 1, 2));
        }
        return fitness;
    }
}