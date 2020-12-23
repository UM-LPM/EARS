package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/259-gramacy-lee-s-function-no-02
 */
public class GramacyLee2 extends Problem {

    public GramacyLee2() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -1.5));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.5));
        name = "GramacyLee2";

        optimum[0] = new double[]{-0.707106776321847, -3.324529260087811 * 10e-9};
    }

    @Override
    public double eval(double[] x) {
        return x[0] * exp(-pow(x[0], 2) - pow(x[1], 2));
    }

    @Override
    public double getGlobalOptimum() {
        return -0.428881942480353;
    }
}