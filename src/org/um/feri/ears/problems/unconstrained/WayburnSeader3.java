package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/279-wayburn-seader-s-function-no-03
 */
public class WayburnSeader3 extends DoubleProblem {

    public WayburnSeader3() {
        super("WayburnSeader3", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 500.0));

        decisionSpaceOptima[0] = new double[]{5.146896745324582, 6.839589743000071};
        objectiveSpaceOptima[0] = 19.105879794567979;
    }

    @Override
    public double eval(double[] x) {
        return 2 * pow(x[0], 3) / 3.0 - 8 * pow(x[0], 2) + 33 * x[0] - x[0] * x[1] + 5 +
                pow(pow(x[0] - 4, 2) + pow(x[1] - 5, 2) - 4, 2);
    }
}