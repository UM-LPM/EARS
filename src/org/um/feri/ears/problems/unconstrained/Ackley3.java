package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
import static java.lang.Math.pow;
/*
http://benchmarkfcns.xyz/benchmarkfcns/ackleyn3fcn.html
 */
public class Ackley3 extends Problem {

    public Ackley3() {
        super(2, 0, 2);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -32.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 32.0));
        name = "Ackley3";

        optimum[0] = new double[]{0.682584587365898, -0.36075325513719};
        optimum[1] = new double[]{-0.682584587365898, -0.36075325513719};
    }

    @Override
    public double eval(double[] x) {
        double fitness = -200.0 * exp(-0.02 * sqrt(pow(x[0], 2) + pow(x[1], 2))) + 5 * exp(cos(3 * x[0]) + sin(3 * x[1]));
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -195.62902823841935;
    }
}