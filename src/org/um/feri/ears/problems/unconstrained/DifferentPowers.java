package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
Black-Box Optimization Benchmarking
http://mantella.info/optimisation-problems/black-box-optimisation-benchmark/#bbob-lunacekbirastriginfunction-arma-uword
 */


public class DifferentPowers extends DoubleProblem {

    public DifferentPowers(int d) {
        super("Different Powers", d, 1, 1, 0);

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));
    }

    @Override
    public double eval(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < numberOfDimensions; ++i) {
            double exponent = 2.0 + 4.0 * (double) i / ((double) numberOfDimensions - 1.0);
            sum += pow(abs(x[i]), exponent);
        }
        return sqrt(sum);
    }
}