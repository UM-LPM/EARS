package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/42-chichinadze-s-function
http://infinity77.net/global_optimization/test_functions_nd_C.html#go_benchmark.Chichinadze
 */
public class Chichinadze extends DoubleProblem {

    public Chichinadze() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -30.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 30.0));
        name = "Chichinadze";

        decisionSpaceOptima[0] = new double[]{6.189866586965680, 0.5};
        objectiveSpaceOptima[0] = -42.94438701899098;
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0], 2) - 12 * x[0] + 8 * sin(5 * PI * x[0] / 2) + 10 * cos(PI * x[0] / 2) + 11 - (0.2 * sqrt(5) / exp(0.5 * (pow(x[1] - 0.5, 2))));
    }
}