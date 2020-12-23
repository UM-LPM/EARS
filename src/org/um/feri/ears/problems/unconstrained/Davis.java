package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/47-davis-function
 */
public class Davis extends Problem {

    public Davis() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Davis";
    }

    @Override
    public double eval(double[] x) {
        return pow(pow(x[0], 2) + pow(x[1], 2), 0.25)
                * (pow(sin(50 * pow(3 * pow(x[0], 2) + pow(x[1], 2), 0.1)), 2) + 1);
    }
}