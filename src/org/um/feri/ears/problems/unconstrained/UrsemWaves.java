package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/132-ursem-wave-function
http://infinity77.net/global_optimization/test_functions_nd_U.html#go_benchmark.UrsemWaves
 */
public class UrsemWaves extends DoubleProblem {

    public UrsemWaves() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 2.0));
        name = "UrsemWaves";

        lowerLimit.set(0, -0.9);
        upperLimit.set(0, 1.2);

        lowerLimit.set(1, -1.2);
        upperLimit.set(1, 1.2);

        decisionSpaceOptima[0] = new double[]{-0.605689494589848, -1.177561933039789};
        objectiveSpaceOptima[0] = -7.306998731324461;
    }

    @Override
    public double eval(double[] x) {
        return -pow(0.3 * x[0], 3) + (pow(x[1], 2) - 4.5 * pow(x[1], 2)) * x[0] * x[1] +
                4.7 * cos(3 * x[0] - pow(x[1], 2) * (2 + x[0])) * sin(2.5 * PI * x[0]);
    }
}