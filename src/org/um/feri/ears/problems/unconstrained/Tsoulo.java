package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/106-tsoulos-function
 */
public class Tsoulo extends Problem {

    public Tsoulo() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.0));
        name = "Tsoulo";
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(x[0], 2) + pow(x[1], 2) - cos(18 * x[0]) - cos(18 * x[1]);
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -2.0;
    }
}