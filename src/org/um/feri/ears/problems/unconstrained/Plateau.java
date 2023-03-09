package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.floor;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Plateau
 */

public class Plateau extends DoubleProblem {

    public Plateau() {
        super("Plateau", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.12));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.12));
        objectiveSpaceOptima[0] = 30.0;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += floor(abs(x[i]));
        }
        return 30.0 + fitness;
    }
}