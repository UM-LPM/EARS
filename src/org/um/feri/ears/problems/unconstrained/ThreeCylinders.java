package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/157-three-cylinders-function
 */
public class ThreeCylinders extends Problem {

    public ThreeCylinders() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
        name = "ThreeCylinders";
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

    @Override
    public double getGlobalOptimum() {
        return 1.05;
    }
}