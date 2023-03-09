package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/278-wayburn-seader-s-function-no-02
http://infinity77.net/global_optimization/test_functions_nd_W.html#go_benchmark.WayburnSeader02
 */
public class WayburnSeader2 extends DoubleProblem {

    public WayburnSeader2() {
        super("WayburnSeader2", 2, 2, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 500.0));

        decisionSpaceOptima[0] = new double[]{0.200138974728779, 1};
        decisionSpaceOptima[1] = new double[]{0.424861025271221, 1};
    }

    @Override
    public double eval(double[] x) {
        return pow(1.613 - 4 * pow(x[0] - 0.3125, 2) - 4 * pow(x[1] - 1.625, 2), 2) + pow(x[1] - 1, 2);
    }
}