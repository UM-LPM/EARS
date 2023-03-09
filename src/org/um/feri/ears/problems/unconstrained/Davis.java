package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/47-davis-function
 */
public class Davis extends DoubleProblem {

    public Davis() {
        super("Davis", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
    }

    @Override
    public double eval(double[] x) {
        return pow(pow(x[0], 2) + pow(x[1], 2), 0.25)
                * (pow(sin(50 * pow(3 * pow(x[0], 2) + pow(x[1], 2), 0.1)), 2) + 1);
    }
}