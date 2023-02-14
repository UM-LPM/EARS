package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/55-freudenstein-roth-s-function
http://infinity77.net/global_optimization/test_functions_nd_F.html#go_benchmark.FreudensteinRoth
 */
public class FreudensteinRoth extends DoubleProblem {

    public FreudensteinRoth() {
        super("FreudensteinRoth", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        decisionSpaceOptima[0] = new double[]{5.0, 4.0};
    }

    @Override
    public double eval(double[] x) {
        return pow(-13 + x[0] + ((5 - x[1]) * x[1] - 2) * x[1], 2) + pow(-29 + x[0] + ((x[1] + 1) * x[1] - 14) * x[1], 2);
    }
}