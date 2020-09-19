package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_Y.html#go_benchmark.YaoLiu04
 */
public class YaoLiu4 extends Problem {

    public YaoLiu4() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
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