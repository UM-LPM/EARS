package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/**
 * https://www.sfu.ca/~ssurjano/branin.html
 */

public class Eggholder extends Problem {

    public Eggholder() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -512.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 512.0));
        name = "Eggholder";

        optimum[0][0] = 512.0;
        optimum[0][1] = 404.2319;
    }

    @Override
    public double eval(double[] x) {
        double fitness = -(x[1] + 47.0) * sin(sqrt(abs(x[1] + x[0] / 2.0 + 47.0)))
                - x[0] * sin(sqrt(abs(x[0] - (x[1] + 47.0))));
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -959.6406627106155;
    }
}