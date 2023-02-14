package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/59-kearfott-s-function
 */
public class Kearfott extends DoubleProblem {

    public Kearfott() {
        super(2, 4, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -3.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 4.0));
        name = "Kearfott";

        decisionSpaceOptima[0] = new double[]{sqrt(1.5), sqrt(0.5)};
        decisionSpaceOptima[1] = new double[]{-sqrt(1.5), sqrt(0.5)};
        decisionSpaceOptima[2] = new double[]{sqrt(1.5), -sqrt(0.5)};
        decisionSpaceOptima[3] = new double[]{-sqrt(1.5), -sqrt(0.5)};

    }

    @Override
    public double eval(double[] x) {
        return pow(pow(x[0], 2) + pow(x[1], 2) - 2, 2) + pow(pow(x[0], 2) - pow(x[1], 2) - 1, 2);
    }
}