package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;
/*
https://sites.google.com/site/gotestfunctions/multimodal-function-list/onelastrally
 */
public class ReduxSum extends DoubleProblem {

    public ReduxSum() {
        super("ReduxSum", 2, 2, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.0));

        decisionSpaceOptima[0] = new double[]{-1.0, 8.743006318923108E-16};
        decisionSpaceOptima[1] = new double[]{8.743006318923108E-16, -1.0};
        objectiveSpaceOptima[0] = -1.143771333935362E15;
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
}

