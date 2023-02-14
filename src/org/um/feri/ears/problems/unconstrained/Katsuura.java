package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.floor;
import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_K.html#go_benchmark.Katsuura
 */
public class Katsuura extends DoubleProblem {

    public Katsuura() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Katsuura";
        objectiveSpaceOptima[0] = 1.0;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 1.0;
        int d = 32;
        double sum;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum = 0;
            for (int k = 1; k <= d; k++) {
                sum += floor(pow(2, k) * x[i]) * pow(2, -k);
            }
            fitness *= 1.0 + (i + 1.0) * sum;
        }
        return fitness;
    }
}