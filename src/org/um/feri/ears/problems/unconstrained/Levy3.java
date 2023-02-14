package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_L.html#go_benchmark.Levy03
 */

public class Levy3 extends DoubleProblem {

    public Levy3() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Levy3";

        Arrays.fill(decisionSpaceOptima[0], 1);
    }

    @Override
    public double eval(double[] x) {

        double[] y = new double[numberOfDimensions];
        for (int i = 0; i < numberOfDimensions; i++) {
            y[i] = 1.0 + (x[i] - 1) / 4.0;
        }

        double sum = 0;
        for (int i = 0; i < numberOfDimensions - 1; i++) {
            sum += pow(y[i] - 1, 2) * (1 + 10 * pow(Math.sin(PI * y[i] + 1), 2));
        }

        return pow(sin(PI * y[0]), 2) + sum + pow(y[numberOfDimensions - 1] - 1, 2); //* (1 + Math.pow(Math.sin(2 * Math.PI * y[numberOfDimensions - 1]), 2))
    }
}