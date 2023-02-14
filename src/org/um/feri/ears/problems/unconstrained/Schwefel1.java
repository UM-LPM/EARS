package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schwefel01

 */
public class Schwefel1 extends DoubleProblem {

    public Schwefel1() {
        super("Schwefel1", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double alpha = sqrt(PI);
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(x[i], 2);
        }
        return pow(fitness, alpha);
    }
}