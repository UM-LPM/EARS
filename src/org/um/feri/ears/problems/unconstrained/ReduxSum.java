package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;
/*
https://sites.google.com/site/gotestfunctions/multimodal-function-list/onelastrally
 */
public class ReduxSum extends Problem {

    public ReduxSum() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.0));
        name = "ReduxSum";
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
        return fitness;
    }
    //TODO check optimum
}