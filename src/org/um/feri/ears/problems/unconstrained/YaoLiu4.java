package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.max;

/*
http://infinity77.net/global_optimization/test_functions_nd_Y.html#go_benchmark.YaoLiu04
 */
public class YaoLiu4 extends DoubleProblem {

    public YaoLiu4() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "YaoLiu4";
    }

    @Override
    public double eval(double[] x) {
        double fitness = x[0];
        for (int i = 1; i < numberOfDimensions; i++) {
            fitness = max(fitness, x[i]);
        }
        return abs(fitness);
    }
}