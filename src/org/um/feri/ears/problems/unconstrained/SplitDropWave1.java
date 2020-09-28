package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

public class SplitDropWave1 extends Problem {
    public SplitDropWave1() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -3.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 3.0));
        name = "SplitDropWave1";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;

        fitness = Math.cos(Math.pow(x[0],2) + Math.pow(x[1],2))+ 2 * Math.exp(-10 * Math.pow(x[1],2));

        return fitness;
    }
}