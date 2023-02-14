package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/157-three-cylinders-function
 */
public class ThreeCylinders extends DoubleProblem {

    public ThreeCylinders() {
        super("ThreeCylinders", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));
        objectiveSpaceOptima[0] = 1.05;
    }

    @Override
    public double eval(double[] x) {
        double fitness;
        double r1 = sqrt(pow(x[0] - 3, 2) + pow(x[1] - 2, 2));
        double r2 = sqrt(pow(x[0] - 4, 2) + pow(x[1] - 4, 2));
        double r3 = sqrt(pow(x[0] - 1, 2) + pow(x[1] - 3, 2));
        if (r1 <= 0.75)
            fitness = 1;
        else if (r2 <= 0.375)
            fitness = 1.05;
        else if (r3 <= 0.25)
            fitness = 1.05;
        else
            fitness = 0;
        return fitness;
    }
}