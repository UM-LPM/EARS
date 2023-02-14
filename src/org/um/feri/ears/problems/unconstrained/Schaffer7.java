package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/101-schaffer-s-function-no-7
 */
public class Schaffer7 extends DoubleProblem {
    public Schaffer7() {
        super("Schaffer7", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
    }

    @Override
    public double eval(double[] x) {
        return pow(pow(x[0], 2) + pow(x[1], 2), 0.25) * (50. * pow(pow(x[0], 2) + pow(x[1], 2), 0.1) + 1);
    }
}