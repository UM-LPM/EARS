package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/109-zettl-s-function
http://infinity77.net/global_optimization/test_functions_nd_Z.html#go_benchmark.Zettl
 */

public class Zettl extends DoubleProblem {

    public Zettl() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));
        name = "Zettl";

        decisionSpaceOptima[0] = new double[]{-0.02989597760285287, 0};
        objectiveSpaceOptima[0] = -0.003791237220468656;
    }

    @Override
    public double eval(double[] x) {
        return 0.25 * x[0] + pow(pow(x[0], 2) - 2 * x[0] + pow(x[1], 2), 2);
    }
}