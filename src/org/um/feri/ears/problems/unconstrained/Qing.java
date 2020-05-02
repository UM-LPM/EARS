package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
/*
http://infinity77.net/global_optimization/test_functions_nd_Q.html#go_benchmark.Qing
http://benchmarkfcns.xyz/benchmarkfcns/qingfcn.html
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/185-qing-s-function
 */
public class Qing extends Problem {

    public Qing() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "Qing";

        for (int i = 0; i < numberOfDimensions; i++) {
            optimum[0][i] = sqrt(i + 1);
        }

    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(pow(x[i], 2) - (i + 1), 2);
        }
        return fitness;
    }
}