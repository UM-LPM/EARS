package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/110-chen-s-v-function
 */
public class ChensVfunction extends DoubleProblem {

    double b = 0.001;

    public ChensVfunction() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "ChensVfunction";

        minimize = false;
        decisionSpaceOptima[0] = new double[]{0.388888888888889, 0.722222222222222};
        objectiveSpaceOptima[0] = 2000.0;
    }

    @Override
    public double eval(double[] x) {
        return (b / (pow(b, 2) + pow(x[0] - 0.4 * x[1] - 0.1, 2)))
                + (b / (pow(b, 2) + pow(2 * x[0] + x[1] - 1.5, 2)));
    }
}