package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/151-mishra-s-function-no-6
http://infinity77.net/global_optimization/test_functions_nd_M.html#go_benchmark.Mishra06
 */
public class Mishra6 extends DoubleProblem {

    public Mishra6() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Mishra6";

        decisionSpaceOptima[0] = new double[]{2.886307215440481, 1.823260331422321};
        objectiveSpaceOptima[0] = -2.283949838474759;
    }

    @Override
    public double eval(double[] x) {
        return -log(pow(pow(sin(pow(cos(x[0]) + cos(x[1]), 2)), 2) - pow(cos(pow(sin(x[0]) + sin(x[1]), 2)), 2) + x[0], 2)) + 0.1 * (pow(x[0] - 1, 2) + pow(x[1] - 1, 2));
    }
}