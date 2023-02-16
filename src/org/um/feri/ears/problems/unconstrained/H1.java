package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/155-h1-filter
 */
public class H1 extends DoubleProblem {

    public H1() {
        super("H1", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -25.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 25.0));

        objectiveMaximizationFlags[0] = true;

        decisionSpaceOptima[0] = new double[]{PI * (36.0 / 13.0), PI * (28.0 / 13.0)};
        objectiveSpaceOptima[0] = 2.0;
    }

    @Override
    public double eval(double[] x) {
        double num = pow(sin(x[0] - x[1] / 8), 2) + pow(sin(x[1] + x[0] / 8), 2);
        double den = sqrt(pow(x[0] - (36 * PI / 13), 2) + pow(x[1] - (28 * PI / 13), 2)) + 1;
        return num / den;
    }
}