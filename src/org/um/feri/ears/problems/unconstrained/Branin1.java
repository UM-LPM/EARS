package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/**
 * https://www.sfu.ca/~ssurjano/branin.html
 * http://infinity77.net/global_optimization/test_functions_nd_B.html#go_benchmark.Branin01
 * https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/13-branin-s-rcos-function-no-1
 */

public class Branin1 extends Problem {

    public Branin1() {
        super(2, 0, 3);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        name = "Branin1";

        lowerLimit.set(0, -5.0);
        upperLimit.set(0, 10.0);

        lowerLimit.set(1, 0.0);
        upperLimit.set(1, 15.0);

        optimum[0] = new double[]{-PI, 12.275};
        optimum[1] = new double[]{PI, 2.275};
        optimum[2] = new double[]{3 * PI, 2.425};
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(x[1] - (5.1 / (4 * PI * PI)) * x[0] * x[0] + (5.0 / PI) * x[0] - 6, 2) + 10 * (1 - 1.0 / (8.0 * PI)) * cos(x[0]) + 10;
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return 0.39788735772973816;
    }
}
