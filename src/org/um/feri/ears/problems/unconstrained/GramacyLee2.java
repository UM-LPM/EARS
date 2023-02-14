package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/259-gramacy-lee-s-function-no-02
 */
public class GramacyLee2 extends DoubleProblem {

    public GramacyLee2() {
        super("GramacyLee2", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -1.5));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.5));

        decisionSpaceOptima[0] = new double[]{-0.707106776321847, -3.324529260087811 * 10e-9};
        objectiveSpaceOptima[0] = -0.428881942480353;
    }

    @Override
    public double eval(double[] x) {
        return x[0] * exp(-pow(x[0], 2) - pow(x[1], 2));
    }
}