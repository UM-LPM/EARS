package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.floor;
import static java.lang.Math.pow;

/*
https://al-roomi.org/benchmarks/unconstrained/n-dimensions/193-step-function-no-2
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Step
 */
public class Step2 extends DoubleProblem {
    public Step2(int d) {
        super(d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Step1";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(floor(x[i] + 0.5), 2);
        }
        return fitness;
    }
}
