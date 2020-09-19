package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.StretchedV
 */
public class StretchedVSineWave extends Problem {

    public StretchedVSineWave() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "StretchedVSineWave";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double t;
        for (int i = 0; i < numberOfDimensions - 1; i++) {
            t = pow(x[i + 1], 2) + pow(x[i], 2);
            fitness += pow(t, 0.25) * (pow(sin(50.0 * pow(t, 0.1)), 2) + 0.1);
        }

        return fitness;
    }
}