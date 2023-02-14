package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://benchmarkfcns.xyz/benchmarkfcns/happycatfcn.html
 */
public class HappyCat extends DoubleProblem {

    public HappyCat() {
        super("HappyCat", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 2.0));

        Arrays.fill(decisionSpaceOptima[0], -1.0);
    }

    @Override
    public double eval(double[] x) {
        double alpha = 0.5;
        double sum = 0.0, power = 0.0;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum += x[i];
            power += pow(x[i], 2);
        }
        return Math.pow(power - numberOfDimensions, 2 * alpha) + (0.5 * power + sum) / numberOfDimensions + 0.5;
    }
}