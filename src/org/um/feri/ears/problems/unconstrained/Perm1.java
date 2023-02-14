package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.PermFunction01
 */
public class Perm1 extends DoubleProblem {

    double beta = 0.5;

    public Perm1(int d) {
        super(d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -numberOfDimensions * 1.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, numberOfDimensions * 1.0 + 1));
        name = "Perm1";

        for (int i = 0; i < numberOfDimensions; i++) {
            decisionSpaceOptima[0][i] = i + 1;
        }
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum = 0;
            for (int j = 0; j < numberOfDimensions; j++) {
                sum += (pow(j + 1, (i + 1)) + beta) * (pow(x[j] / (j + 1), (i + 1)) - 1);
            }
            fitness += pow(sum, 2);
        }
        return fitness;
    }
}
