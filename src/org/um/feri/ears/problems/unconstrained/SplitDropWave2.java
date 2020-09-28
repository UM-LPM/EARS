package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

public class SplitDropWave2 extends Problem {
    public SplitDropWave2() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
        name = "SplitDropWave2";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;

        fitness = Math.cos(Math.pow(x[0],2) + Math.pow(x[1],2)) + 2 * Math.exp(-10 * Math.pow(x[1],2)) + 0.5 * x[1];

        return fitness;
    }
}