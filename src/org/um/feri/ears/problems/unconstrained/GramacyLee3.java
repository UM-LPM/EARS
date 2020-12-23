package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/260-gramacy-lee-s-function-no-03
 */
public class GramacyLee3 extends Problem {

    public GramacyLee3() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -1.5));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.5));
        name = "GramacyLee3";

        Arrays.fill(optimum[0], -1.041);
    }

    @Override
    public double eval(double[] x) {
        double f1 = exp(-pow(x[0] - 1, 2)) + exp(-0.8 * pow(x[0] + 1, 2)) - 0.05 * sin(8 * (x[0] + 0.1));
        double f2 = exp(-pow(x[1] - 1, 2)) + exp(-0.8 * pow(x[1] + 1, 2)) - 0.05 * sin(8 * (x[1] + 0.1));
        return -f1 * f2;
    }

    @Override
    public double getGlobalOptimum() {
        return -1.126871604339345;
    }
}