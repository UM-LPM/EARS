package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
http://www.geatbx.com/docu/fcnindex-01.html#P86_3059
 */
public class RosenbrockD3 extends DoubleProblem {

    public RosenbrockD3() {
        super("Rosenbrock d3", 3, 1, 1, 0);

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -1.2));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 1.2));

        Arrays.fill(decisionSpaceOptima[0], 1);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < (numberOfDimensions - 1); i++) {
            fitness += 100 * (x[i + 1] - x[i] * x[i]) * (x[i + 1] - x[i] * x[i]) + (1 - x[i]) * (1 - x[i]);
        }
        return fitness;
    }
}
