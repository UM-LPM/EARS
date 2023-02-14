package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_T.html#go_benchmark.Trefethen
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/105-trefethen-s-function
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/105-trefethen-s-function
 */

public class Trefethen extends DoubleProblem {

    public Trefethen() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Trefethen";

        decisionSpaceOptima[0] = new double[]{-0.02440307923, 0.2106124261};
        objectiveSpaceOptima[0] = -3.3068686474752305;
    }

    @Override
    public double eval(double[] x) {
        return 0.25 * pow(x[0], 2) + 0.25 * pow(x[1], 2) + exp(sin(50.0 * x[0])) - sin(10.0 * x[0] +
                10.0 * x[1]) + sin(60.0 * exp(x[1])) + sin(70.0 * sin(x[0])) + sin(sin(80.0 * x[1]));
    }
}