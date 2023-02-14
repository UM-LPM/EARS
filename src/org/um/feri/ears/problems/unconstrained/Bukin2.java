package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_B.html#go_benchmark.Bukin06
wrong optimum given at link
 */

public class Bukin2 extends DoubleProblem {

    public Bukin2() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        name = "Bukin2";

        lowerLimit.set(0, -15.0);
        upperLimit.set(0, -5.0);

        lowerLimit.set(1, -3.0);
        upperLimit.set(1, 3.0);

        decisionSpaceOptima[0][0] = -15;
        decisionSpaceOptima[0][1] = 0.0;
        objectiveSpaceOptima[0] = -124.75;
    }

    @Override
    public double eval(double[] x) {
        return 100.0 * (pow(x[1], 2) - 0.01 * pow(x[0], 2) + 1.0) + 0.01 * pow(x[0] + 10, 2);
    }
}