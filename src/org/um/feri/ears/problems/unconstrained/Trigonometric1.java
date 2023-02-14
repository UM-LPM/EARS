package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_T.html#go_benchmark.Trigonometric01

 */

public class Trigonometric1 extends DoubleProblem {

    public Trigonometric1() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, PI));
        name = "Trigonometric1";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum = 0;
        for (int j = 0; j < numberOfDimensions; j++) {
            sum += cos(x[j]);
        }
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(numberOfDimensions - sum + (i + 1) * (1 - cos(x[i]) - sin(x[i])), 2);
        }
        return fitness;
    }
}