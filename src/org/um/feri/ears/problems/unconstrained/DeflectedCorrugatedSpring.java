package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://al-roomi.org/benchmarks/unconstrained/n-dimensions/238-deflected-corrugated-spring-function
http://infinity77.net/global_optimization/test_functions_nd_D.html#go_benchmark.DeflectedCorrugatedSpring
 */
public class DeflectedCorrugatedSpring extends Problem {

    double k = 5.0, alpha = 5.0;

    public DeflectedCorrugatedSpring() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.0 * alpha));
        name = "DeflectedCorrugatedSpring";

        Arrays.fill(optimum[0], alpha);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0, sum = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum += pow(x[i] - alpha, 2);
        }

        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(x[i] - alpha, 2) - cos(k * sqrt(sum));
        }
        fitness *= 0.1;
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -0.2;
    }
}