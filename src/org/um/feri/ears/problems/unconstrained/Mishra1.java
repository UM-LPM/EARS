package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;
/*
http://infinity77.net/global_optimization/test_functions_nd_M.html#go_benchmark.Mishra01
 */
public class Mishra1 extends Problem {

    public Mishra1() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.0));
        name = "Mishra1";

        Arrays.fill(optimum[0], 1.0);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum = 0;
        for (int i = 0; i < numberOfDimensions - 1; i++) {
            sum += x[i];
        }
        sum = numberOfDimensions - sum;
        fitness = pow(1 + sum, sum);
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return 2.0;
    }
}