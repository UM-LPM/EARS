package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Plateau
 */

public class Plateau extends Problem {

    public Plateau() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.12));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.12));
        name = "Plateau";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += floor(abs(x[i]));
        }
        return 30.0 + fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return 30.0;
    }
}