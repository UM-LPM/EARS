package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/*
http://www.geatbx.com/docu/fcnindex-01.html#P86_3059
 */
public class RosenbrockD3 extends Problem {

    public RosenbrockD3() {
        super(3, 0);

        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -1.2));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.2));
        name = "Rosenbrock d3";

        Arrays.fill(optimum[0], 1);
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
