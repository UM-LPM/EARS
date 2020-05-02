package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/101-schaffer-s-function-no-7
 */
public class Schaffer7 extends Problem {
    public Schaffer7() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Schaffer7";
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(pow(x[0], 2) + pow(x[1], 2), 0.25) * (50. * pow(pow(x[0], 2) + pow(x[1], 2), 0.1) + 1);
        return fitness;
    }
}