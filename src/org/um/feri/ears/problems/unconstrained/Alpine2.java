package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/*
http://benchmarkfcns.xyz/benchmarkfcns/alpinen2fcn.html
http://infinity77.net/global_optimization/test_functions_nd_A.html#go_benchmark.Alpine02
 */
public class Alpine2 extends DoubleProblem {

    public Alpine2() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Alpine2";

        Arrays.fill(decisionSpaceOptima[0], 7.917);
        objectiveSpaceOptima[0] = Math.pow(2.808, numberOfDimensions);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 1.0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness *= sqrt(x[i]) * sin(x[i]);
        }
        return fitness;
    }
}