package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/michal.html
 */

public class Michalewicz5 extends DoubleProblem {

    public Michalewicz5() {
        super("Michalewicz5", 5, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, PI));
        objectiveSpaceOptima[0] = -4.68765817;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        int m = 10;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += sin(x[i]) * pow(sin((i + 1) * x[i] * x[i] / PI), 2 * m);
        }
        return -fitness;
    }
}
