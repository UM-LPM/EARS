package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.max;

/*
http://benchmarkfcns.xyz/benchmarkfcns/schwefel221fcn.html
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/189-schwefel-s-function-no-2-21
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schwefel21
 */
public class Schwefel221 extends Problem {

    public Schwefel221() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Schwefel221"; // also known as MaxMod
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