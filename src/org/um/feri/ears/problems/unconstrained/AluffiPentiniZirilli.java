package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_Z.html#go_benchmark.Zirilli
https://al-roomi.org/benchmarks/unconstrained/2-dimensions/26-aluffi-pentini-s-or-zirilli-s-function
 */
public class AluffiPentiniZirilli extends DoubleProblem {

    public AluffiPentiniZirilli() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "AluffiPentiniZirilli";

        decisionSpaceOptima[0] = new double[]{-1.046680576580755, 0};
        objectiveSpaceOptima[0] = -0.3523860738000341;
    }

    @Override
    public double eval(double[] x) {
        return 0.25 * pow(x[0], 4) - 0.5 * pow(x[0], 2) + 0.1 * x[0] + 0.5 * pow(x[1], 2);
    }
}