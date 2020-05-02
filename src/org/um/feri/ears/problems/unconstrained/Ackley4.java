package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
/*
http://benchmarkfcns.xyz/benchmarkfcns/ackleyn4fcn.html
 */
public class Ackley4 extends Problem {

    public Ackley4() {
        super(2, 0, 2);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -35.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 35.0));
        name = "Ackley4"; // also known as Modified Ackley Function

        optimum[0] = new double[]{-1.51, -0.755};
        optimum[1] = new double[]{1.51, -0.755};
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions - 1; i++) {
            fitness += exp(-0.2) * sqrt(pow(x[i], 2) + pow(x[i + 1], 2))
                    + 3.0 * (cos(2 * x[i]) + sin(2 * x[i + 1]));
        }
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -4.590101633799122;
    }
}