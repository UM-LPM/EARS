package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_Q.html#go_benchmark.Quintic
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/240-quintic-function
 */
public class Quintic extends Problem {

    public Quintic() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Quintic";

        optimum[0] = new double[]{-1.0, 2.0};
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += abs(pow(x[i], 5) - 3 * pow(x[i], 4) + 4 * pow(x[i], 3) + 2 * pow(x[i], 2) - 10 * x[i] - 4);
        }
        return fitness;
    }
}