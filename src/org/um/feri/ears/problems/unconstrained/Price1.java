package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/28-becker-lago-s-function
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Price01
 */
public class Price1 extends DoubleProblem {

    public Price1() {
        super(2, 4, 1, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "Price1"; // also known as Becker-Lago's Function

        decisionSpaceOptima[0] = new double[]{5.0, 5.0};
        decisionSpaceOptima[1] = new double[]{5.0, -5.0};
        decisionSpaceOptima[2] = new double[]{-5.0, 5.0};
        decisionSpaceOptima[3] = new double[]{-5.0, -5.0};
    }

    @Override
    public double eval(double[] x) {
        return pow(abs(x[0]) - 5, 2) + pow(abs(x[1]) - 5, 2);
    }
}