package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
Efficient and Robust Gradient Enhanced Kriging Emulators
 */
public class Herbie extends DoubleProblem {

    public Herbie() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 2.0));
        name = "Herbie";

        decisionSpaceOptima[0] = new double[]{2.0, 2.0};
        objectiveSpaceOptima[0] = 0.17057261679141927;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 1.0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness *= exp(-pow(x[i] - 1, 2)) + exp(-0.8 * pow(x[i] + 1, 2)) - 0.05 * sin(8 * (x[i] + 0.1));
        }
        return fitness;
    }
}