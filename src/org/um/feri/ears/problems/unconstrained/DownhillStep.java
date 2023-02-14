package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/114-downhill-step-function
 */
public class DownhillStep extends DoubleProblem {

    public DownhillStep() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "DownhillStep";
        objectiveSpaceOptima[0] = 9.0;
    }

    @Override
    public double eval(double[] x) {
        return floor(10.0 * (10.0 - exp(-pow(x[0], 2) - 3 * pow(x[1], 2)))) / 10.0;
    }
}