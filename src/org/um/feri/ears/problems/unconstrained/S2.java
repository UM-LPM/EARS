package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/117-s2-function
 */
public class S2 extends DoubleProblem {

    public S2() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "S2";
        //infinite number of global minima at (X1, 0.7)
        objectiveSpaceOptima[0] = 2.0;
    }

    @Override
    public double eval(double[] x) {
        return 2 + pow(x[1] - 0.7, 2);
    }
}