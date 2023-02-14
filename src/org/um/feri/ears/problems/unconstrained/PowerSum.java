package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.sfu.ca/~ssurjano/powersum.html
 */

public class PowerSum extends DoubleProblem {

    public double[] b;

    public PowerSum() {
        super(4, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, numberOfDimensions * 1.0));
        name = "PowerSum";
        b = new double[]{8, 18, 44, 114};

        decisionSpaceOptima[0] = new double[]{1.0, 2.0, 2.0, 3.0};
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum = 0;
            for (int j = 0; j < numberOfDimensions; j++) {
                sum += pow(x[j], i + 1);
            }
            fitness += pow(sum - b[i], 2);
        }
        return fitness;
    }
}
