package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/150-mishra-s-function-no-5
http://infinity77.net/global_optimization/test_functions_nd_M.html#go_benchmark.Mishra05
 */
public class Mishra5 extends DoubleProblem {

    public Mishra5() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Mishra5";

        decisionSpaceOptima[0] = new double[]{-1.986820662153768, -10};
        objectiveSpaceOptima[0] = -1.019829519930943;
    }

    @Override
    public double eval(double[] x) {
        return pow(pow(sin(pow(cos(x[0]) + cos(x[1]), 2)), 2) + pow(cos(pow(sin(x[0]) + sin(x[1]), 2)), 2) + x[0], 2) + 0.01 * (x[0] + 10 * x[1]);
    }
}