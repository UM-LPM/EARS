package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
import static java.lang.Math.abs;

/*
http://infinity77.net/global_optimization/test_functions_nd_U.html#go_benchmark.Ursem04
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/131-ursem-function-no-4
 */
public class Ursem4 extends Problem {

    public Ursem4() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.0));
        name = "Ursem4";
    }

    @Override
    public double eval(double[] x) {
        double fitness = -3 * sin(0.5 * PI * x[0] + 0.5 * PI) * ((2 - sqrt(pow(x[0], 2) + pow(x[1], 2))) / 4.0);
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -1.5;
    }
}