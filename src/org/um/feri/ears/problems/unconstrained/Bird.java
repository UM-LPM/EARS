package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/29-bird-function
http://infinity77.net/global_optimization/test_functions_nd_B.html#go_benchmark.Bird
 */
public class Bird extends Problem {

    public Bird() {
        super(2, 0, 2);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2.0 * Math.PI));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.0 * Math.PI));
        name = "Bird";

        optimum[0] = new double[]{4.701055751981055, 3.152946019601391};
        optimum[1] = new double[]{-1.582142172055011, -3.130246799635430};
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(x[0] - x[1], 2) + exp(pow(1 - sin(x[0]), 2)) * cos(x[1]) + exp(pow(1 - cos(x[1]), 2)) * sin(x[0]);
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -106.7645367198034;
    }
}