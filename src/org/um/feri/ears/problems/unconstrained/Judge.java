package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/133-judge-s-function
http://infinity77.net/global_optimization/test_functions_nd_J.html#go_benchmark.Judge
 */
public class Judge extends DoubleProblem {

    private static final double[] A = new double[]{4.284, 4.149, 3.877, 0.533, 2.211, 2.389, 2.145, 3.231, 1.998, 1.379, 2.106, 1.428, 1.011, 2.179, 2.858, 1.388, 1.651, 1.593, 1.046, 2.152};
    private static final double[] B = new double[]{0.286, 0.973, 0.384, 0.276, 0.973, 0.543, 0.957, 0.948, 0.543, 0.797, 0.936, 0.889, 0.006, 0.828, 0.399, 0.617, 0.939, 0.784, 0.072, 0.889};
    private static final double[] C = new double[]{0.645, 0.585, 0.310, 0.058, 0.455, 0.779, 0.259, 0.202, 0.028, 0.099, 0.142, 0.296, 0.175, 0.180, 0.842, 0.039, 0.103, 0.620, 0.158, 0.704};

    public Judge() {
        super("Judge", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        decisionSpaceOptima[0] = new double[]{0.864787285816574, 1.235748499036571};
        objectiveSpaceOptima[0] = 16.081730132960381;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int k = 0; k < 20; k++) {
            fitness += pow((x[0] + B[k] * x[1] + C[k] * pow(x[1], 2)) - A[k], 2);
        }
        return fitness;
    }
}