package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Shekel10
 */
public class Shekel10 extends DoubleProblem {

    public double[][] a;
    public double[] c;


    public Shekel10() {
        super(4, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Shekel10";

        Arrays.fill(decisionSpaceOptima[0], 4);
        objectiveSpaceOptima[0] = -10.53628372;

        a = new double[][]{
                {4, 4, 4, 4},
                {1, 1, 1, 1},
                {8, 8, 8, 8},
                {6, 6, 6, 6},
                {3, 7, 3, 7},
                {2, 9, 2, 9},
                {5, 5, 3, 3},
                {8, 1, 8, 1},
                {6, 2, 6, 2},
                {7, 3.6, 7, 3.6},
        };
        c = new double[]{0.1, 0.2, 0.2, 0.4, 0.4, 0.6, 0.3, 0.7, 0.5, 0.5};

    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum;
        for (int i = 0; i < 10; i++) {
            sum = 0;
            for (int j = 0; j < numberOfDimensions; j++) {
                sum += pow(x[j] - a[i][j], 2);
            }
            fitness += pow(c[i] + sum, -1);
        }
        return -fitness;
    }
}
