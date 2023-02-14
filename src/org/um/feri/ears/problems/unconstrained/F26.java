package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/253-f26-function
 */
public class F26 extends DoubleProblem {

    public F26() {
        super("F26", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        decisionSpaceOptima[0] = new double[]{1.046680526600663, 0};
        objectiveSpaceOptima[0] = -0.35238607380003634;
    }

    @Override
    public double eval(double[] x) {
        return 0.25 * pow(x[0], 4) - 0.5 * pow(x[0], 2) - 0.1 * x[0] + 0.5 * pow(x[1], 2);
    }
}