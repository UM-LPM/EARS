package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Sargan
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/244-sargan-s-function
 */
public class Sargan extends DoubleProblem {

    public Sargan() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Sargan";
    }

    @Override
    public double eval(double[] x) {
        return 2 * (pow(x[0], 2) + 0.4 * (x[0] * x[1])) + 2 * (pow(x[1], 2) + 0.4 * (pow(x[1], 2)));
    }
}