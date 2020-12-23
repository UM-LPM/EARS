package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/58-hosaki-s-function
http://infinity77.net/global_optimization/test_functions_nd_H.html#go_benchmark.Hosaki
 */
public class Hosaki extends Problem {

    public Hosaki() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Hosaki";

        optimum[0] = new double[]{4.0, 2.0};
    }

    @Override
    public double eval(double[] x) {
        return (1 - 8 * x[0] + 7 * pow(x[0], 2) - (7.0 / 3.0) * pow(x[0], 3) + (1.0 / 4.0) * pow(x[0], 4)) * pow(x[1], 2) * exp(-x[1]);
    }

    @Override
    public double getGlobalOptimum() {
        return -2.345811576101292;
    }
}