package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://benchmarkfcns.xyz/benchmarkfcns/ackleyn3fcn.html
 */
public class Ackley3 extends DoubleProblem {

    public Ackley3() {
        super(2, 2, 1, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -32.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 32.0));
        name = "Ackley3";

        decisionSpaceOptima[0] = new double[]{0.682584587365898, -0.36075325513719};
        decisionSpaceOptima[1] = new double[]{-0.682584587365898, -0.36075325513719};
        objectiveSpaceOptima[0] = -195.62902823841935;
    }

    @Override
    public double eval(double[] x) {
        return -200.0 * exp(-0.02 * sqrt(pow(x[0], 2) + pow(x[1], 2))) + 5 * exp(cos(3 * x[0]) + sin(3 * x[1]));
    }
}