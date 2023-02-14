package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.sfu.ca/~ssurjano/stybtang.html
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.StyblinskiTang
http://benchmarkfcns.xyz/benchmarkfcns/styblinskitankfcn.html
 */

public class StyblinskiTang extends DoubleProblem {

    public StyblinskiTang(int d) {
        super(d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));
        name = "Styblinski Tang";

        Arrays.fill(decisionSpaceOptima[0], -2.90353401818596);
        objectiveSpaceOptima[0] = -39.16616570377142 * numberOfDimensions;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(x[i], 4) - 16 * pow(x[i], 2) + 5 * x[i];
        }
        return fitness / 2.0;
    }
}