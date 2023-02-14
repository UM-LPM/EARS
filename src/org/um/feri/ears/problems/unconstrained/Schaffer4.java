package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schaffer04
http://benchmarkfcns.xyz/benchmarkfcns/schaffern4fcn.html
 */
public class Schaffer4 extends DoubleProblem {
    public Schaffer4() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Schaffer4";

        decisionSpaceOptima[0] = new double[]{0, 1.253115};
        objectiveSpaceOptima[0] = 0.29243850703298857;
    }

    @Override
    public double eval(double[] x) {
        return 0.5 + (pow(cos(sin(pow(x[0], 2) - pow(x[1], 2))), 2) - 0.5) /
                (1 + 0.001 * pow((pow(x[0], 2) + pow(x[1], 2)), 2));
    }
}