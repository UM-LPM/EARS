package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Price03
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/160-price-s-function-no-3-modified-rosenbrock-s-or-price-rosenbrock-s-function
 */
public class Price3 extends DoubleProblem {

    public Price3() {
        super(2, 2, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -50.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 50.0));
        name = "Price3"; // also known as Modified Rosenbrock's or Price-Rosenbrock's Function

        decisionSpaceOptima[0] = new double[]{1.0, 1.0};
        decisionSpaceOptima[1] = new double[]{0.341307503353524, 0.116490811845416};
    }

    @Override
    public double eval(double[] x) {
        return 100 * pow(x[1] - pow(x[0], 2), 2) + pow(6.4 * pow(x[1] - 0.5, 2) - x[0] - 0.6, 2);
    }
}