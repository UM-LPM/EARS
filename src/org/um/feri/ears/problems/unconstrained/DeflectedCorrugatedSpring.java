package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://al-roomi.org/benchmarks/unconstrained/n-dimensions/238-deflected-corrugated-spring-function
http://infinity77.net/global_optimization/test_functions_nd_D.html#go_benchmark.DeflectedCorrugatedSpring
 */
public class DeflectedCorrugatedSpring extends DoubleProblem {

    double k = 5.0, alpha = 5.0;

    public DeflectedCorrugatedSpring() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 2.0 * alpha));
        name = "DeflectedCorrugatedSpring";

        Arrays.fill(decisionSpaceOptima[0], alpha);
        objectiveSpaceOptima[0] = -0.2;
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
        return fitness * 0.1;
    }
}