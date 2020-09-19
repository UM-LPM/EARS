package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.PermFunction02
 */
public class Perm2 extends Problem {

    double beta = 0.5;

    public Perm2(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -numberOfDimensions * 1.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, numberOfDimensions * 1.0 + 1));
        name = "Perm2";

        /*for (int i = 0; i < numberOfDimensions; i++) {
            optimum[0][i] = 1.0 / (i+1);
        }*/
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum = 0;
        for (int k = 0; k < numberOfDimensions; k++) {
            sum = 0;
            for (int j = 0; j < numberOfDimensions; j++) {
                sum += (j + 1 + beta) * (pow(x[j], (k + 1)) - 1.0 / (j + 1));
            }
            fitness += pow(sum, 2);
        }
        return fitness;
    }
}