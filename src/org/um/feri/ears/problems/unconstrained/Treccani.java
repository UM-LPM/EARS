package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_T.html#go_benchmark.Treccani
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/70-treccani-s-function
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/70-treccani-s-function
 */

public class Treccani extends DoubleProblem {

    public Treccani() {
        super(2, 2, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));
        name = "Treccani";

        decisionSpaceOptima[0] = new double[]{-2.0, 0.0};
        decisionSpaceOptima[1] = new double[]{0.0, 0.0};
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0], 4) + 4 * pow(x[0], 3) + 4 * pow(x[0], 2) + pow(x[1], 2);
    }
}