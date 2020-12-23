package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_M.html#go_benchmark.Mishra02
 */
public class Mishra2 extends Problem {

    public Mishra2() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.0));
        name = "Mishra2";

        Arrays.fill(optimum[0], 1.0);
    }

    @Override
    public double eval(double[] x) {
        double sum = 0;
        for (int i = 0; i < numberOfDimensions - 1; i++) {
            sum += (x[i] + x[i + 1]) / 2.0;
        }
        sum = numberOfDimensions - sum;
        return pow(1 + sum, sum);
    }

    @Override
    public double getGlobalOptimum() {
        return 2.0;
    }
}