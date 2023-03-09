package org.um.feri.ears.problems.unconstrained;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/181-mishra-function-no-7-or-factorial-function
http://infinity77.net/global_optimization/test_functions_nd_M.html#go_benchmark.Mishra07
 */
public class Mishra7 extends DoubleProblem {

    public Mishra7() {
        super("Mishra7", 2, 1, 1, 0);
        // also known as Factorial
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
    }

    @Override
    public double eval(double[] x) {
        double fact = CombinatoricsUtils.factorial(numberOfDimensions);
        double prod = 1.0;
        for (int i = 0; i < numberOfDimensions; i++) {
            prod *= x[i];
        }

        return pow(prod - fact, 2);
    }
}