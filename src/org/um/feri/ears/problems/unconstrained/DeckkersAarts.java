package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
/*
http://infinity77.net/global_optimization/test_functions_nd_D.html#go_benchmark.DeckkersAarts
http://benchmarkfcns.xyz/benchmarkfcns/deckkersaartsfcn.html
 */

public class DeckkersAarts extends DoubleProblem {

    public DeckkersAarts() {
        super("DeckkersAarts", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -20.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 20.0));

        decisionSpaceOptima[0] = new double[]{0.0, 15.0};
        objectiveSpaceOptima[0] = -24777.0;
    }

    @Override
    public double eval(double[] x) {
        return pow(10, 5) * pow(x[0], 2) + pow(x[1], 2) - pow(pow(x[0], 2) + pow(x[1], 2), 2) + pow(10, -5) * pow(pow(x[0], 2) + pow(x[1], 2), 4);
    }
}