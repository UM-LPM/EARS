package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.cos;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/106-tsoulos-function
 */
public class Tsoulo extends DoubleProblem {

    public Tsoulo() {
        super("Tsoulo", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.0));
        objectiveSpaceOptima[0] = -2.0;
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0], 2) + pow(x[1], 2) - cos(18 * x[0]) - cos(18 * x[1]);
    }
}