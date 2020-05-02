package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_C.html#go_benchmark.Cube
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/119-cube-function
 */
public class Cube extends Problem {

    public Cube() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Cube";

        Arrays.fill(optimum[0], 1.0);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 100 * pow(x[1] - pow(x[0], 3), 2) + pow(1 - x[0], 2);
        return fitness;
    }
}