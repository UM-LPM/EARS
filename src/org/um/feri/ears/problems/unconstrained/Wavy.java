package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://al-roomi.org/benchmarks/unconstrained/n-dimensions/280-w-wavy-function
http://infinity77.net/global_optimization/test_functions_nd_W.html#go_benchmark.Wavy
 */
public class Wavy extends DoubleProblem {

    public Wavy() {
        super("Wavy", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -PI));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, PI));
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double k = 10.0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += cos(k * x[i]) * exp(-pow(x[i], 2) * 0.5);
        }
        return 1 - (1.0 / numberOfDimensions) * fitness;
    }
}