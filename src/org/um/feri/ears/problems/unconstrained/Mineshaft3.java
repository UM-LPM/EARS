package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/115-mineshaft-function-no-3
 */
public class Mineshaft3 extends Problem {

    public Mineshaft3() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.0));
        name = "Mineshaft3";

        optimum[0] = new double[]{0.8, 1.3};
    }

    @Override
    public double eval(double[] x) {
        double fitness = -5 * exp(-1000 * pow(x[0] - 0.5, 2) - 1000 * pow(x[1] - 0.3, 2))
                - 7 * exp(-2000 * pow(x[0] - 0.8, 2) - 2000 * pow(x[1] - 1.3, 2));
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -7.0;
    }
}