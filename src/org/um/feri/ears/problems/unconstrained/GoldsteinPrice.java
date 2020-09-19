package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/goldpr.html
http://benchmarkfcns.xyz/benchmarkfcns/goldsteinpricefcn.html
 */

public class GoldsteinPrice extends Problem {
    public GoldsteinPrice() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.0));
        name = "GoldsteinPrice";

        optimum[0][0] = 0;
        optimum[0][1] = -1;
    }

    @Override
    public double eval(double[] x) {
        double fitness = (1 + pow(x[0] + x[1] + 1, 2) * (19 - 14 * x[0] + 3 * x[0] * x[0] - 14 * x[1] + 6 * x[0] * x[1] + 3 * x[1] * x[1])) *
                (30 + pow(2 * x[0] - 3 * x[1], 2) * (18 - 32 * x[0] + 12 * x[0] * x[0] + 48 * x[1] - 36 * x[0] * x[1] + 27 * x[1] * x[1]));
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return 3.0;
    }
}
