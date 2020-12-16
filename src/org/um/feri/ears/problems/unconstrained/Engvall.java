package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/116-engvall-s-function
 */
public class Engvall extends Problem {

    public Engvall() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2000.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2000.0));
        name = "Engvall";

        optimum[0][0] = 1.0;
        optimum[0][1] = 0.0;
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0], 4) + pow(x[1], 4) + 2 * pow(x[0], 2) * pow(x[1], 2) - 4 * x[0] + 3;
    }
}