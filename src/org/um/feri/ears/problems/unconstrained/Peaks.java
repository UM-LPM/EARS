package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/63-peaks-function
 */
public class Peaks extends DoubleProblem {

    public Peaks() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -4.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 4.0));
        name = "Peaks";

        decisionSpaceOptima[0] = new double[]{0.228279999979237, -1.625531071954464};
        objectiveSpaceOptima[0] = -6.551133332622496;
    }

    @Override
    public double eval(double[] x) {
        double fa = 3 * pow(1 - x[0], 2) * exp(-pow(x[0], 2) - pow(x[1] + 1, 2));
        double fb = 10 * ((x[0] / 5) - pow(x[0], 3) - pow(x[1], 5)) * exp(-pow(x[0], 2) - pow(x[1], 2));
        double fc = (1.0 / 3.0) * exp(-(pow(x[0] + 1, 2)) - pow(x[1], 2));
        return fa - fb - fc;
    }
}