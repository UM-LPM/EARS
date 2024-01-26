package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/michal.html
 */
public class Michalewicz2 extends DoubleProblem {

    public Michalewicz2() {
        super("Michalewicz", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, PI));

        decisionSpaceOptima[0][0] = 2.20290552;
        decisionSpaceOptima[0][1] = 1.57079633;
        objectiveSpaceOptima[0] = -1.80130341;
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
