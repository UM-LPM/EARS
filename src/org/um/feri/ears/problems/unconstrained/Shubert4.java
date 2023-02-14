package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.cos;

/*
http://benchmarkfcns.xyz/benchmarkfcns/shubert4fcn.html
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Shubert04
 */
public class Shubert4 extends DoubleProblem {
    public Shubert4(int d) {
        super(d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Shubert4";
        //multiple global optima
        objectiveSpaceOptima[0] = -25.740858;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            for (int j = 1; j <= 5; j++) {
                fitness += j * cos(((j + 1) * x[i]) + j);
            }
        }
        return fitness;
    }
}