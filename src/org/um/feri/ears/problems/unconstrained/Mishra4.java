package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_M.html#go_benchmark.Mishra04
different equation
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/149-mishra-s-function-no-4
 */
public class Mishra4 extends DoubleProblem {

    public Mishra4() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Mishra4";

        decisionSpaceOptima[0] = new double[]{-9.941127263635860, -9.999571661999983};
        objectiveSpaceOptima[0] = -0.199406970088833;
    }

    @Override
    public double eval(double[] x) {
        return pow(abs(sin(sqrt(abs(pow(x[0], 2) + x[1])))), 0.5) + (x[0] + x[1]) / 100.0;
    }
}