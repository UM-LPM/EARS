package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.max;

/*
http://benchmarkfcns.xyz/benchmarkfcns/schwefel221fcn.html
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/189-schwefel-s-function-no-2-21
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schwefel21
 */
public class Schwefel221 extends DoubleProblem {

    public Schwefel221() {
        super("Schwefel221", 2, 1, 1, 0);
        // also known as MaxMod
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
    }

    @Override
    public double eval(double[] x) {
        double fitness = x[0];
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness = max(abs(x[i]), abs(fitness));
        }
        return fitness;
    }
}