package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/100-schaffer-s-function-no-6
 */
public class Schaffer6 extends DoubleProblem {
    public Schaffer6() {
        super("Schaffer6", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
    }

    @Override
    public double eval(double[] x) {
        return 0.5 + (pow(sin(sqrt(pow(x[0], 2) + pow(x[1], 2))), 2) - 0.5)
                / pow(1 + 0.001 * (pow(x[0], 2) + pow(x[1], 2)), 2);
    }
}