package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/58-hosaki-s-function
http://infinity77.net/global_optimization/test_functions_nd_H.html#go_benchmark.Hosaki
 */
public class Hosaki extends DoubleProblem {

    public Hosaki() {
        super("Hosaki", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        decisionSpaceOptima[0] = new double[]{4.0, 2.0};
        objectiveSpaceOptima[0] = -2.345811576101292;
    }

    @Override
    public double eval(double[] x) {
        return (1 - 8 * x[0] + 7 * pow(x[0], 2) - (7.0 / 3.0) * pow(x[0], 3) + (1.0 / 4.0) * pow(x[0], 4)) * pow(x[1], 2) * exp(-x[1]);
    }
}