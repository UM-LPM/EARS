package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.sfu.ca/~ssurjano/trid.html
 */

public class Trid2 extends DoubleProblem {

    public Trid2() {
        super("Trid2", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -pow(numberOfDimensions, 2)));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, pow(numberOfDimensions, 2)));

        for (int i = 0; i < numberOfDimensions; i++) {
            decisionSpaceOptima[0][i] = (i + 1) * (numberOfDimensions + 1 - (i + 1));
        }
        objectiveSpaceOptima[0] = -(numberOfDimensions * (numberOfDimensions + 4.0) * (numberOfDimensions - 1)) / 6.0;
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
}