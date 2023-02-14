package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/107-ursem-function-no-1
http://infinity77.net/global_optimization/test_functions_nd_U.html#go_benchmark.Ursem01
 */
public class Ursem1 extends DoubleProblem {

    public Ursem1() {
        super("Ursem1", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));

        lowerLimit.set(0, -2.5);
        upperLimit.set(0, 3.0);

        lowerLimit.set(1, -2.0);
        upperLimit.set(1, 2.0);

        decisionSpaceOptima[0] = new double[]{1.697136443570341, 0.0};
        objectiveSpaceOptima[0] = -4.816814063734822;
    }

    @Override
    public double eval(double[] x) {
        return -sin(2 * x[0] - 0.5 * PI) - 3 * cos(x[1]) - 0.5 * x[0];
    }
}