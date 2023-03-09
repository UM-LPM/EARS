package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

/*
A Literature Survey of Benchmark Functions For GlobalOptimization Problems
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Pathological
 */

public class Pathological extends DoubleProblem {

    public Pathological() {
        super("Pathological", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double f1, f2;

        //http://infinity77.net
        /*for (int i = 0; i < numberOfDimensions - 1; i++) {
            f1 = Math.pow(Math.sin(Math.sqrt(100 * Math.pow(x[i + 1], 2) + Math.pow(x[i], 2))), 2) - 0.5;
            f2 = 0.001 * Math.pow(x[i] - x[i + 1], 4) + 0.5;
            result += f1 / f2;
        }*/

        for (int i = 0; i < numberOfDimensions - 1; i++) {
            f1 = Math.pow(Math.sin(Math.sqrt(100 * Math.pow(x[i + 1], 2) + Math.pow(x[i], 2))), 2) - 0.5;
            f2 = 1 + 0.001 * Math.pow(Math.pow(x[i], 2) - 2 * x[i] * x[i + 1] + Math.pow(x[i + 1], 2), 2);
            fitness += f1 / f2 + 0.5;
        }
        return fitness;
    }

    //http://infinity77.net
    /*
    @Override
    public double getOptimumEval() {
        return -1.99600798403;
    }*/
}