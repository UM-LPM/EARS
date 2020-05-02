package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_V.html#go_benchmark.Vincent
 */
public class Vincent extends Problem {

    public Vincent() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.25));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Vincent";

        Arrays.fill(optimum[0], 7.70628098);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += sin(10 * log(x[i]));
        }
        return -fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -numberOfDimensions;
    }
}