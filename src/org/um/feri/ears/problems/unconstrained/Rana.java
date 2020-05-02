package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_R.html#go_benchmark.Rana
 */
public class Rana extends Problem {

    public Rana() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.000001));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.000001));
        name = "Rana";

        Arrays.fill(optimum[0], -500.0);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += x[i] * sin(sqrt(abs(x[0] - x[i] + 1))) * cos(sqrt(abs(x[0] + x[i] + 1))) + (x[0] + 1) * sin(sqrt(abs(x[0] + x[i] + 1))) * cos(sqrt(abs(x[0] - x[i] + 1)));
        }
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -928.5478554047827;
    }
}