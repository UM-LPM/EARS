package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://al-roomi.org/benchmarks/unconstrained/2-dimensions/7-shekel-s-foxholes-function
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/7-shekel-s-foxholes-function
*/

public class Foxholes extends Problem {

    public double[][] a;

    public Foxholes() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -65.536));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 65.536));
        name = "Shekel's Foxholes";

        Arrays.fill(optimum[0], -31.97833);

        a = new double[][]{{-32, -32},
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

    @Override
    public double getGlobalOptimum() {
        return 0.998003838;
    }
}
