package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/103-stenger-s-function
 */
public class Stenger extends Problem {

    public Stenger() {
        super(2, 0, 2);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 4.0));
        name = "Stenger";

        optimum[0] = new double[]{0.0, 0.0};
        optimum[1] = new double[]{1.695415196279268, 0.718608171943623};
    }

    @Override
    public double eval(double[] x) {
        return pow(pow(x[0], 2) - 4 * x[1], 2) + pow(pow(x[1], 2) - 2 * x[0] + 4 * x[1], 2);
    }
}