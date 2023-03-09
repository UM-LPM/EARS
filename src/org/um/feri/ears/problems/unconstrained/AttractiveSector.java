package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

/*
Black-Box Optimization Benchmarking
http://mantella.info/optimisation-problems/black-box-optimisation-benchmark/#bbob-lunacekbirastriginfunction-arma-uword
 */

public class AttractiveSector extends DoubleProblem {

    public AttractiveSector(int d) {
        super("Attractive Sector", d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));

        Arrays.fill(decisionSpaceOptima[0], 5);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0.0;
        for (int i = 0; i < numberOfDimensions; ++i) {
            if (decisionSpaceOptima[0][i] * x[i] > 0.0) {
                fitness += pow(100.0, 2) * pow(x[i], 2);
            } else {
                fitness += pow(x[i], 2);
            }
        }
        return fitness;
    }
}
