package org.um.feri.ears.problems.unconstrained;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/181-mishra-function-no-7-or-factorial-function
http://infinity77.net/global_optimization/test_functions_nd_M.html#go_benchmark.Mishra07
 */
public class Mishra7 extends Problem {

    public Mishra7() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Mishra7"; // also known as Factorial
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double fact = CombinatoricsUtils.factorial(numberOfDimensions);
        double prod = 1.0;
        for (int i = 0; i < numberOfDimensions; i++) {
            prod *= x[i];
        }
        fitness = pow(prod - fact, 2);

        return fitness;
    }
}