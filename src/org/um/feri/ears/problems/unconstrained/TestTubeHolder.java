package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/104-test-tube-holder-function
http://infinity77.net/global_optimization/test_functions_nd_T.html#test-functions-n-d-test-functions-t
 */

public class TestTubeHolder extends Problem {

    public TestTubeHolder() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Test Tube Holder";

        optimum[0] = new double[]{-PI / 2.0, 0.0};
    }

    @Override
    public double eval(double[] x) {
        return -4 * abs(exp(abs(cos((pow(x[0], 2) + pow(x[1], 2)) / 200))) * sin(x[0]) * cos(x[1]));
    }

    @Override
    public double getGlobalOptimum() {
        return -10.872299901558;
    }
}