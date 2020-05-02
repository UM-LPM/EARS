package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/133-judge-s-function
http://infinity77.net/global_optimization/test_functions_nd_J.html#go_benchmark.Judge
 */
public class Judge extends Problem {

    private double[] A;
    private double[] B;
    private double[] C;

    public Judge() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Judge";

        A = new double[]{4.284, 4.149, 3.877, 0.533, 2.211, 2.389, 2.145, 3.231, 1.998, 1.379, 2.106, 1.428, 1.011, 2.179, 2.858, 1.388, 1.651, 1.593, 1.046, 2.152};
        B = new double[]{0.286, 0.973, 0.384, 0.276, 0.973, 0.543, 0.957, 0.948, 0.543, 0.797, 0.936, 0.889, 0.006, 0.828, 0.399, 0.617, 0.939, 0.784, 0.072, 0.889};
        C = new double[]{0.645, 0.585, 0.310, 0.058, 0.455, 0.779, 0.259, 0.202, 0.028, 0.099, 0.142, 0.296, 0.175, 0.180, 0.842, 0.039, 0.103, 0.620, 0.158, 0.704};

        optimum[0] = new double[]{0.864787285816574, 1.235748499036571};
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int k = 0; k < 20; k++) {
            fitness += pow((x[0] + B[k] * x[1] + C[k] * pow(x[1], 2)) - A[k], 2);
        }
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return 16.081730132960381;
    }
}