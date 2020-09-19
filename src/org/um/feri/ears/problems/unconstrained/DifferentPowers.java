package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;
import static java.lang.Math.*;

/*
Black-Box Optimization Benchmarking
http://mantella.info/optimisation-problems/black-box-optimisation-benchmark/#bbob-lunacekbirastriginfunction-arma-uword
 */


public class DifferentPowers extends Problem {

    public DifferentPowers(int d) {
        super(d, 0);

        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));

        name = "Different Powers";
    }

    @Override
    public double eval(double[] x) {
        double fitness;
        double sum = 0.0;
        for (int i = 0; i < numberOfDimensions; ++i) {
            double exponent = 2.0 + 4.0 * (double) i / ((double) numberOfDimensions - 1.0);
            sum += pow(abs(x[i]), exponent);
        }
        fitness = sqrt(sum);
        return fitness;
    }
}