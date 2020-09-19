package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;
import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/trid.html
 */

public class Trid10 extends Problem {

    public Trid10() {
        super(10, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -pow(numberOfDimensions, numberOfDimensions)));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, pow(numberOfDimensions, numberOfDimensions)));
        name = "Trid10";

        for (int i = 0; i < numberOfDimensions; i++) {
            optimum[0][i] = (i + 1) * (numberOfDimensions + 1 - (i + 1));
        }
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum1 = 0, sum2 = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum1 += pow((x[i] - 1), 2);
        }
        for (int i = 1; i < numberOfDimensions; i++) {
            sum2 += x[i] * x[i - 1];
        }
        fitness = sum1 - sum2;
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -(numberOfDimensions * (numberOfDimensions + 4.0) * (numberOfDimensions - 1.0)) / 6.0;
    }
}
