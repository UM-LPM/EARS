package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

/*
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/167-ex3-csendes-or-infinity-function
http://infinity77.net/global_optimization/test_functions_nd_C.html#go_benchmark.Csendes
 */
public class Csendes extends DoubleProblem {

    public Csendes() {
        super("Csendes", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.0));
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(x[i], 6) * (2 + sin(1.0 / x[i]));
        }
        return fitness;
    }
}