package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

/*
Black-Box Optimization Benchmarking
http://mantella.info/optimisation-problems/black-box-optimisation-benchmark/#bbob-lunacekbirastriginfunction-arma-uword
 */

public class AttractiveSector extends Problem {

    public AttractiveSector(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
        name = "Attractive Sector";

        Arrays.fill(optimum[0], 5);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0.0;
        for (int i = 0; i < numberOfDimensions; ++i) {
            if (optimum[0][i] * x[i] > 0.0) {
                fitness += pow(100.0, 2) * pow(x[i], 2);
            } else {
                fitness += pow(x[i], 2);
            }
        }
        return fitness;
    }
}
