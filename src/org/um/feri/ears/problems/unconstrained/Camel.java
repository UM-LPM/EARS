package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/31-camel-function
 */
public class Camel extends DoubleProblem {

    public Camel() {
        super("Camel", 2, 2, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 2.0));

        decisionSpaceOptima[0][0] = 1.5;
        decisionSpaceOptima[0][1] = 0.0;

        decisionSpaceOptima[1][0] = -1.5;
        decisionSpaceOptima[1][1] = 0.0;
        objectiveSpaceOptima[0] = -7.0625;
    }

    @Override
    public double eval(double[] x) {
        return -((-pow(x[0], 4) + 4.5 * pow(x[0], 2) + 2) / (exp(2 * pow(x[1], 2))));
    }
}