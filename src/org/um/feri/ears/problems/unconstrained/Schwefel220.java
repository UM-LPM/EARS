package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;

/*
http://benchmarkfcns.xyz/benchmarkfcns/schwefel220fcn.html
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schwefel20
 */
public class Schwefel220 extends Problem {

    public Schwefel220() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Schwefel220";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += abs(x[i]);
        }
        return fitness;
    }
}