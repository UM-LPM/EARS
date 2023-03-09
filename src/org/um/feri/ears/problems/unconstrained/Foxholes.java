package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://al-roomi.org/benchmarks/unconstrained/2-dimensions/7-shekel-s-foxholes-function
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/7-shekel-s-foxholes-function
*/

public class Foxholes extends DoubleProblem {

    private static final double[][] a = {{-32, -32},
            {-16, -32},
            {0, -32},
            {16, -32},
            {32, -32},
            {-32, -16},
            {-16, -16},
            {0, -16},
            {16, -16},
            {32, -16},
            {-32, 0},
            {-16, 0},
            {0, 0},
            {16, 0},
            {32, 0},
            {-32, 16},
            {-16, 16},
            {0, 16},
            {16, 16},
            {32, 16},
            {-32, 32},
            {-16, 32},
            {0, 32},
            {16, 32},
            {32, 32}
    };

    public Foxholes() {
        super("Shekel's Foxholes", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -65.536));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 65.536));

        Arrays.fill(decisionSpaceOptima[0], -31.97833);
        objectiveSpaceOptima[0] = 0.998003838;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum;
        for (int j = 0; j < 25; j++) {
            sum = 0;
            for (int i = 0; i < numberOfDimensions; i++) {
                sum += pow(x[i] - a[j][i], 6);
            }
            sum += j + 1;
            fitness += 1.0 / sum;
        }
        fitness += 1.0 / 500.0;
        return pow(fitness, -1);
    }
}
