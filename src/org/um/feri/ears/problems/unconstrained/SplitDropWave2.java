package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

public class SplitDropWave2 extends DoubleProblem {
    public SplitDropWave2() {
        super("SplitDropWave2", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));
    }

    @Override
    public double eval(double[] x) {
        return cos(pow(x[0], 2) + pow(x[1], 2)) + 2 * exp(-10 * pow(x[1], 2)) + 0.5 * x[1];
    }
}