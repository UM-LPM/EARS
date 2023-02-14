package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.sin;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Shubert03
http://benchmarkfcns.xyz/benchmarkfcns/shubert3fcn.html
 */
public class Shubert3 extends DoubleProblem {
    public Shubert3(int d) {
        super("Shubert3", d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        //multiple global optima
        objectiveSpaceOptima[0] = -29.6733337;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            for (int j = 1; j <= 5; j++) {
                fitness += j * sin(((j + 1) * x[i]) + j);
            }
        }
        return fitness;
    }
}
