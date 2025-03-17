package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/184-salomon-s-function
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Salomon
 */
public class Salomon extends DoubleProblem {

    public Salomon(int d) {
        super("Salomon" + d, d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
    }

    @Override
    public double eval(double[] x) {
        double norm = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            norm += pow(x[i], 2);
        }
        norm = sqrt(norm);
        return 1 - cos(2 * PI * norm) + 0.1 * norm;
    }
}