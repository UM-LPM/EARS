package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/branin.html
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/52-bukin-s-function-no-6
http://infinity77.net/global_optimization/test_functions_nd_B.html#go_benchmark.Bukin06
http://benchmarkfcns.xyz/benchmarkfcns/bukinn6fcn.html
 */

public class Bukin6 extends DoubleProblem {

    public Bukin6() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        name = "Bukin6";

        lowerLimit.set(0, -15.0);
        upperLimit.set(0, -5.0);

        lowerLimit.set(1, -3.0);
        upperLimit.set(1, 3.0);

        decisionSpaceOptima[0][0] = -10;
        decisionSpaceOptima[0][1] = 1.0;
    }

    @Override
    public double eval(double[] x) {
        return 100.0 * sqrt(abs(x[1] - 0.01 * pow(x[0], 2))) + 0.01 * abs(x[0] + 10);
    }
}