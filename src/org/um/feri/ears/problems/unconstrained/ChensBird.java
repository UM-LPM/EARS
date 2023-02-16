package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/111-chen-s-bird-function
 */
public class ChensBird extends DoubleProblem {

    double b = 0.001;

    public ChensBird() {
        super("ChensBird", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 500.0));

        objectiveMaximizationFlags[0] = true;

        Arrays.fill(decisionSpaceOptima[0], 0.5);
        objectiveSpaceOptima[0] = 2000.0039999840003;
    }

    @Override
    public double eval(double[] x) {
        return (b / (pow(b, 2) + pow(pow(x[0], 2) + pow(x[1], 2) - 1, 2)))
                + (b / (pow(b, 2) + pow(pow(x[0], 2) + pow(x[1], 2) - 0.5, 2)))
                + (b / (pow(b, 2) + pow(x[0] - x[1], 2)));
    }
}