package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_B.html#go_benchmark.Bukin04
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/51-bukin-s-function-no-4
 */

public class Bukin4 extends Problem {

    public Bukin4() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        name = "Bukin4";

        lowerLimit.set(0, -15.0);
        upperLimit.set(0, -5.0);

        lowerLimit.set(1, -3.0);
        upperLimit.set(1, 3.0);

        optimum[0][0] = -10.0;
        optimum[0][1] = 0.0;
    }

    @Override
    public double eval(double[] x) {
        return 100.0 * pow(x[1], 2) + 0.01 * abs(x[0] + 10);
    }
}