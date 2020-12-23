package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/43-complex-function
 */
public class Complex extends Problem {

    public Complex() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.0));
        name = "Complex";

        optimum[0][0] = 1.0;
        optimum[0][1] = 0.0;
    }

    @Override
    public double eval(double[] x) {
        return pow(pow(x[0], 3) - 3 * x[0] * pow(x[1], 2) - 1, 2)
                + pow(3 * x[1] * pow(x[0], 2) - pow(x[1], 3), 2);
    }
}