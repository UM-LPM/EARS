package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;
import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_T.html#go_benchmark.Treccani
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/70-treccani-s-function
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/70-treccani-s-function
 */

public class Treccani extends Problem {

    public Treccani() {
        super(2, 0, 2);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
        name = "Treccani";

        optimum[0] = new double[]{-2.0, 0.0};
        optimum[1] = new double[]{0.0, 0.0};
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(x[0], 4) + 4 * pow(x[0], 3) + 4 * pow(x[0], 2) + pow(x[1], 2);
        return fitness;
    }
}