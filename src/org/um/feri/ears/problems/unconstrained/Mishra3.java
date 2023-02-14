package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_M.html#go_benchmark.Mishra03
different equation
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/138-mishra-s-function-no-3
 */
public class Mishra3 extends DoubleProblem {

    public Mishra3() {
        super("Mishra3", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));

        //Arrays.fill(optimum[0], -10.0);
        decisionSpaceOptima[0] = new double[]{-8.466613775046579, -9.998521308999999};
        objectiveSpaceOptima[0] = -0.184651333342989;
    }

    @Override
    public double eval(double[] x) {
        //result = sqrt(abs(cos(sqrt(abs(pow(x[0], 2) + pow(x[1], 2)))))) + 0.01 * (x[0] + x[1]);
        return pow(abs(cos(sqrt(abs(pow(x[0], 2) + x[1])))), 0.5) + (x[0] + x[1]) / 100.0;
    }
}