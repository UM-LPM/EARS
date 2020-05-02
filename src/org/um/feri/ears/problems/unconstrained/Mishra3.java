package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_M.html#go_benchmark.Mishra03
different equation
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/138-mishra-s-function-no-3
 */
public class Mishra3 extends Problem {

    public Mishra3() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Mishra3";

        //Arrays.fill(optimum[0], -10.0);
        optimum[0] = new double[]{-8.466613775046579, -9.998521308999999};
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        //result = sqrt(abs(cos(sqrt(abs(pow(x[0], 2) + pow(x[1], 2)))))) + 0.01 * (x[0] + x[1]);

        fitness = pow(abs(cos(sqrt(abs(pow(x[0], 2) + x[1])))), 0.5) + (x[0] + x[1]) / 100.0;
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -0.184651333342989;
    }
}