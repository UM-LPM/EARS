package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.sfu.ca/~ssurjano/spheref.html
http://benchmarkfcns.xyz/benchmarkfcns/spherefcn.html
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Sphere
*/
public class Sphere extends DoubleProblem {
    public Sphere(int d) {
        super("Sphere", d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += +pow(x[i], 2);
        }
        return fitness;
    }
}
