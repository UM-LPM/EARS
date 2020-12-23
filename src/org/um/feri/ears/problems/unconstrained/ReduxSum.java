package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;
/*
https://sites.google.com/site/gotestfunctions/multimodal-function-list/onelastrally
 */
public class ReduxSum extends Problem {

    public ReduxSum() {
        super(2, 0,2);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.0));
        name = "ReduxSum";

        optimum[0] = new double[]{-1.0, 8.743006318923108E-16};
        optimum[1] = new double[]{8.743006318923108E-16, -1.0};
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum += x[i];
        }

        for (int j = 0; j < numberOfDimensions; j++) {
            fitness += sum / x[j];
        }
        if(Double.isNaN(fitness))
            return Double.MAX_VALUE;

        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -1.143771333935362E15;
    }
}

