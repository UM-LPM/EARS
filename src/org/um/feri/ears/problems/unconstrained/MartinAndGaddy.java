package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

public class MartinAndGaddy extends DoubleProblem {
    public MartinAndGaddy() {
        super("MartinAndGaddy", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        Arrays.fill(decisionSpaceOptima[0], 5.0);
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0] - x[1], 2) + pow((x[0] + x[1] - 10.) / 3., 2);
    }
}
