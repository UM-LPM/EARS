package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/72-bartels-conn-s-function
http://infinity77.net/global_optimization/test_functions_nd_B.html#test-functions-n-d-test-functions-b
 */

public class BartelsConn extends DoubleProblem {

    public BartelsConn() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "BartelsConn";
        objectiveSpaceOptima[0] = 1.0;
    }

    @Override
    public double eval(double[] x) {
        return Math.abs(Math.pow(x[0], 2) + Math.pow(x[1], 2) + x[0] * x[1]) + Math.abs(Math.sin(x[0])) + Math.abs(Math.cos(x[1]));
    }
}