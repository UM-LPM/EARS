package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/*
Black-Box Optimization Benchmarking
http://mantella.info/optimisation-problems/black-box-optimisation-benchmark/#bbob-lunacekbirastriginfunction-arma-uword
 */


public class SharpRidge extends DoubleProblem {

    public SharpRidge(int d) {
        super(d, 1, 1, 0);

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));

        name = "SharpRidge";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0.0;
        final double alpha = 100.0;

        for (int i = 1; i < numberOfDimensions; i++) {
            fitness += pow(x[i], 2);
        }
        return pow(x[0], 2) + alpha * sqrt(fitness);
    }
}
