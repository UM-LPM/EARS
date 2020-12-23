package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.floor;

/*
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/195-stepint-function
 */
public class Stepint extends Problem {
    public Stepint(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.12));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.12));
        name = "Stepint";

        //infinite number of global optima
        // -5.12 <= x < -5.0
        Arrays.fill(optimum[0], -5.12);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += floor(x[i]);
        }
        return fitness + 25.0;
    }

    @Override
    public double getGlobalOptimum() {
        return 25.0 - 6.0 * numberOfDimensions;
    }
}
