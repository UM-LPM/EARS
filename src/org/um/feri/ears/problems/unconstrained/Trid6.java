package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.sfu.ca/~ssurjano/trid.html
 */

public class Trid6 extends Problem {

    public Trid6() {
        super(6, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -pow(numberOfDimensions, 2)));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, pow(numberOfDimensions, 2)));
        name = "Trid6";

        for (int i = 0; i < numberOfDimensions; i++) {
            optimum[0][i] = (i + 1) * (numberOfDimensions + 1 - (i + 1));
        }
    }

    @Override
    public double eval(double[] x) {
        double sum1 = 0, sum2 = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum1 += pow((x[i] - 1), 2);
        }
        for (int i = 1; i < numberOfDimensions; i++) {
            sum2 += x[i] * x[i - 1];
        }
        return sum1 - sum2;
    }

    @Override
    public double getGlobalOptimum() {
        return -(numberOfDimensions * (numberOfDimensions + 4.0) * (numberOfDimensions - 1)) / 6.0;
    }
}
