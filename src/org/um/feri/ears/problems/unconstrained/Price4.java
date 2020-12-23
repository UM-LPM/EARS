package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Price04
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/159-price-s-function-no-4
 */
public class Price4 extends Problem {

    public Price4() {
        super(2, 0, 3);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -50.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 50.0));
        name = "Price4";

        optimum[0] = new double[]{0.0, 0.0};
        optimum[1] = new double[]{2.0, 4.0};
        optimum[2] = new double[]{1.464352119663698, -2.506012760781662};
    }

    @Override
    public double eval(double[] x) {
        return pow(2 * pow(x[0], 3) * x[1] - pow(x[1], 3), 2) + pow(6 * x[0] - pow(x[1], 2) + x[1], 2);
    }
}