package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;
import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/mccorm.html
http://benchmarkfcns.xyz/benchmarkfcns/mccormickfcn.html
 */

public class McCormick extends Problem {

    public McCormick() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        name = "McCormick";

        lowerLimit.set(0, -1.5);
        upperLimit.set(0, 4.0);

        lowerLimit.set(1, -3.0);
        upperLimit.set(1, 4.0);

        optimum[0][0] = -0.54719;
        optimum[0][1] = -1.54719;
    }

    @Override
    public double eval(double[] x) {
        double fitness = sin(x[0] + x[1]) + pow(x[0] - x[1], 2) + -1.5 * x[0] + 2.5 * x[1] + 1;
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -1.913222954882274;
    }
}