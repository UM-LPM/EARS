package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/117-s2-function
 */
public class S2 extends Problem {

    public S2() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "S2";
        //infinite number of global minima at (X1, 0.7)
    }

    @Override
    public double eval(double[] x) {
        double fitness = 2 + pow(x[1] - 0.7, 2);
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return 2.0;
    }
}