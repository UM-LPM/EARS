package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;
/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/110-chen-s-v-function
 */
public class ChensVfunction extends Problem {

    public ChensVfunction() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "ChensVfunction";

        minimize = false;
        optimum[0] = new double[]{0.388888888888889, 0.722222222222222};
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double b = 0.001;
        fitness = (b / (pow(b, 2) + pow(x[0] - 0.4 * x[1] - 0.1, 2)))
                + (b / (pow(b, 2) + pow(2 * x[0] + x[1] - 1.5, 2)));
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return 2000.0;
    }
}