package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
http://infinity77.net/global_optimization/test_functions_nd_R.html#go_benchmark.Rosenbrock
http://benchmarkfcns.xyz/benchmarkfcns/rosenbrockfcn.html
http://www.geatbx.com/docu/fcnindex-01.html#P86_3059
 */
public class RosenbrockD2a extends DoubleProblem {

    public RosenbrockD2a() {
        super(2, 1, 1, 0);

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        name = "Rosenbrock d2a";

        Arrays.fill(decisionSpaceOptima[0], 1);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < (numberOfDimensions - 1); i++) {
            fitness += 100 * (x[i + 1] - x[i] * x[i]) * (x[i + 1] - x[i] * x[i]) + (1 - x[i]) * (1 - x[i]);
        }
        return fitness;
    }
}
