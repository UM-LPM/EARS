package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.floor;

/*
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/195-stepint-function
 */
public class Stepint extends DoubleProblem {
    public Stepint(int d) {
        super("Stepint", d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.12));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.12));

        //infinite number of global optima
        // -5.12 <= x < -5.0
        Arrays.fill(decisionSpaceOptima[0], -5.12);
        objectiveSpaceOptima[0] = 25.0 - 6.0 * numberOfDimensions;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += floor(x[i]);
        }
        return fitness + 25.0;
    }
}
