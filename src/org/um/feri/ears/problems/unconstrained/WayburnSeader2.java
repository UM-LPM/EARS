package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/278-wayburn-seader-s-function-no-02
http://infinity77.net/global_optimization/test_functions_nd_W.html#go_benchmark.WayburnSeader02
 */
public class WayburnSeader2 extends Problem {

    public WayburnSeader2() {
        super(2, 0, 2);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "WayburnSeader2";

        optimum[0] = new double[]{0.200138974728779, 1};
        optimum[1] = new double[]{0.424861025271221, 1};
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(1.613 - 4 * pow(x[0] - 0.3125, 2) - 4 * pow(x[1] - 1.625, 2), 2) + pow(x[1] - 1, 2);
        return fitness;
    }
}