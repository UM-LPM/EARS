package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.sfu.ca/~ssurjano/dixonpr.html
 */

public class DixonPrice extends Problem {
    public DixonPrice(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Dixon_Price";

        for (int i = 0; i < numberOfDimensions; i++) {
            double minX = pow(2, -(pow(2, i + 1) - 2) / pow(2, i + 1));
            optimum[0][i] = minX;
        }
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 1; i < numberOfDimensions; i++) {
            fitness += (i + 1) * pow(2 * x[i] * x[i] - x[i - 1], 2);
        }
        return pow(x[0] - 1, 2) + fitness;
    }
}
