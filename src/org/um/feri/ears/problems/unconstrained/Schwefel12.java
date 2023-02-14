package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schwefel02
 */
public class Schwefel12 extends DoubleProblem {

    public Schwefel12() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Schwefel12";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum = 0;
            for (int j = 0; j < i; j++) {
                sum += x[i];
            }
            fitness += pow(sum, 2);
        }
        return fitness;
    }
}