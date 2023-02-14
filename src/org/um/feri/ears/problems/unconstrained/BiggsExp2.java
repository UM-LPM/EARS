package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
https://al-roomi.org/benchmarks/unconstrained/2-dimensions/123-biggs-exp2-function
http://infinity77.net/global_optimization/test_functions_nd_E.html#go_benchmark.Exp2
 */
public class BiggsExp2 extends DoubleProblem {

    public BiggsExp2() {
        super("BiggsExp2", 2, 1, 1, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 20.0));
        name = "BiggsExp2";

        decisionSpaceOptima[0] = new double[]{1, 10.0};
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;

        for (int k = 0; k < 9; k++) {
            fitness += pow(exp(-k * x[0] / 10.0) - 5 * exp(-k * x[1] / 10.0) - exp(-k / 10.0) + 5 * exp(-k), 2);
        }

        return fitness;
    }
}