package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
/*
http://infinity77.net/global_optimization/test_functions_nd_D.html#go_benchmark.Deb02
 */

public class Deb2 extends DoubleProblem {

    public Deb2() {
        super("Deb2", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.0));
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(sin(5 * PI * (x[i] * 0.75 - 0.05)), 6);
        }
        return -(1.0 / numberOfDimensions) * fitness;
    }
}