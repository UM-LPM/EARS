package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_W.html#go_benchmark.Weierstrass
 */
public class Weierstrass extends DoubleProblem {

    public Weierstrass() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -0.5));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.5));
        name = "Weierstrass";
        objectiveSpaceOptima[0] = 4.0;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        int kMax = 20;
        double a = 0.5, b = 3.0;
        double sum1, sum2;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum1 = 0;
            sum2 = 0;
            for (int k = 0; k < kMax; k++) {
                sum1 += pow(a, k) * cos(2 * PI * pow(b, k) * (x[i] + 0.5));
                sum2 += pow(a, k) * cos(PI * pow(b, k));
            }
            fitness += sum1 - numberOfDimensions * sum2;
        }
        return fitness;
    }
}