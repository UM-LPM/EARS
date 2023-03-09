package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.floor;

/*
https://al-roomi.org/benchmarks/unconstrained/n-dimensions/192-step-function-no-1
 */

public class Step1 extends DoubleProblem {
    public Step1(int d) {
        super("Step1", d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += floor(x[i]);
        }
        return fitness;
    }
}
