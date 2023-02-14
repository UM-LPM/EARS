package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_B.html#go_benchmark.Branin02
 */
public class Branin2 extends DoubleProblem {

    public Branin2() {
        super(2, 3, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 15.0));
        name = "Branin2";

        decisionSpaceOptima[0][0] = -3.2;
        decisionSpaceOptima[0][1] = 12.53;
        objectiveSpaceOptima[0] = 5.5590373208591375;
    }

    @Override
    public double eval(double[] x) {
        return pow(x[1] - (5.1 / (4 * PI * PI)) * x[0] * x[0] + (5.0 / PI) * x[0] - 6, 2) + 10 * (1 - 1.0 / (8.0 * PI)) * cos(x[0])
                * cos(x[1]) + log(pow(x[0], 2) + pow(x[1], 2) + 1) + 10;
    }
}