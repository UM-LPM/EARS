package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/107-ursem-function-no-1
http://infinity77.net/global_optimization/test_functions_nd_U.html#go_benchmark.Ursem01
 */
public class Ursem1 extends Problem {

    public Ursem1() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        name = "Ursem1";

        lowerLimit.set(0, -2.5);
        upperLimit.set(0, 3.0);

        lowerLimit.set(1, -2.0);
        upperLimit.set(1, 2.0);

        optimum[0] = new double[]{1.697136443570341, 0.0};
    }

    @Override
    public double eval(double[] x) {
        double fitness = -sin(2 * x[0] - 0.5 * PI) - 3 * cos(x[1]) - 0.5 * x[0];
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -4.816814063734822;
    }
}