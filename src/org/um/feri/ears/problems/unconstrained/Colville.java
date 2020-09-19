package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;
/*
https://www.sfu.ca/~ssurjano/colville.html
 */

public class Colville extends Problem {

    public Colville() {
        super(4, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Colville";

        Arrays.fill(optimum[0], 1);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 100 * (pow(x[0] * x[0] - x[1], 2))
                + pow(x[0] - 1, 2)
                + pow(x[2] - 1, 2)
                + 90 * pow(x[2] * x[2] - x[3], 2)
                + 10.1 * (pow(x[1] - 1, 2) + pow(x[3] - 1, 2))
                + 19.8 * (x[1] - 1) * (x[3] - 1);
        return fitness;
    }
}
