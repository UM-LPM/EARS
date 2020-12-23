package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/46-crowned-cross-function
http://infinity77.net/global_optimization/test_functions_nd_C.html#go_benchmark.CrownedCross
 */
public class CrownedCross extends Problem {

    public CrownedCross() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "CrownedCross";
    }

    @Override
    public double eval(double[] x) {
        return 0.0001 * pow(abs(exp(abs(100 - (sqrt(pow(x[0], 2) + pow(x[1], 2)) / PI))) * sin(x[0]) * sin(x[1])) + 1, 0.1);
    }

    @Override
    public double getGlobalOptimum() {
        return 0.0001;
    }
}