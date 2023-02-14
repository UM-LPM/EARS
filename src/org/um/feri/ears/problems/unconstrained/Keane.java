package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://benchmarkfcns.xyz/benchmarkfcns/kealefcn.html
https://al-roomi.org/benchmarks/unconstrained/2-dimensions/135-keane-s-function
http://infinity77.net/global_optimization/test_functions_nd_K.html#go_benchmark.Keane
 */
public class Keane extends DoubleProblem {

    public Keane() {
        super("Keane", 2, 2, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        decisionSpaceOptima[0] = new double[]{1.393249070031784, 0};
        decisionSpaceOptima[1] = new double[]{0, 1.393249070031784};
        objectiveSpaceOptima[0] = 0.673667521146855;
    }

    @Override
    public double eval(double[] x) {
        double numerator = (pow(sin(x[0] - x[1]), 2) * pow(sin(x[0] + x[1]), 2));
        double denominator = (sqrt(pow(x[0], 2) + pow(x[1], 2)));
        if (denominator == 0)
            denominator = 0.0000001;
        return numerator / denominator;
    }
}