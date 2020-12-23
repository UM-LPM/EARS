package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

/*
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/182-mishra-s-function-no-11-or-amgm-function
http://infinity77.net/global_optimization/test_functions_nd_A.html#go_benchmark.AMGM
 */
public class Mishra11 extends Problem {

    public Mishra11() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Mishra11"; // also known as AMGM - Arithmetic Mean-Geometric Mean

        // optimum is at x1 = x2 = ... =xn where all x are non-negative
    }

    @Override
    public double eval(double[] x) {
        double sum = 0, prod = 1.0;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum += Math.abs(x[i]);
            prod *= Math.abs(x[i]);
        }
        return Math.pow((1.0 / numberOfDimensions) * sum - Math.pow(prod, 1.0 / numberOfDimensions), 2);
    }
}