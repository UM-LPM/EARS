package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_U.html#go_benchmark.Ursem04
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/131-ursem-function-no-4
 */
public class Ursem4 extends DoubleProblem {

    public Ursem4() {
        super("Ursem4", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 2.0));
        objectiveSpaceOptima[0] = -1.5;
    }

    @Override
    public double eval(double[] x) {
        return -3 * sin(0.5 * PI * x[0] + 0.5 * PI) * ((2 - sqrt(pow(x[0], 2) + pow(x[1], 2))) / 4.0);
    }
}