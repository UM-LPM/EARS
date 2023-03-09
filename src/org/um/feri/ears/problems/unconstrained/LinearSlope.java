package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
Black-Box Optimization Benchmarking
http://mantella.info/optimisation-problems/black-box-optimisation-benchmark/#bbob-lunacekbirastriginfunction-arma-uword
 */


public class LinearSlope extends DoubleProblem {

    public LinearSlope(int d) {
        super("Linear Slope", d, 1, 1, 0);

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));

        Arrays.fill(decisionSpaceOptima[0], 5);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0.0;
        final double alpha = 100.0;
        for (int i = 0; i < numberOfDimensions; ++i) {
            double base, exponent, si;

            base = sqrt(alpha);
            exponent = (double) i / ((double) numberOfDimensions - 1);
            if (decisionSpaceOptima[0][i] > 0.0) {
                si = pow(base, exponent);
            } else {
                si = -pow(base, exponent);
            }
            /* boundary handling */
            if (x[i] * decisionSpaceOptima[0][i] < 25.0) {
                fitness += 5.0 * abs(si) - si * x[i];
            } else {
                fitness += 5.0 * abs(si) - si * decisionSpaceOptima[0][i];
            }
        }
        return fitness;
    }
}
