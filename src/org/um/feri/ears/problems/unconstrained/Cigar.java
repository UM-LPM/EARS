package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_C.html#go_benchmark.Cigar
 */
public class Cigar extends Problem {

    public Cigar() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Cigar";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 1; i < numberOfDimensions; i++) {
            fitness += pow(x[i], 2);
        }
        return pow(x[0], 2) + pow(10, 6) * fitness;
    }
}