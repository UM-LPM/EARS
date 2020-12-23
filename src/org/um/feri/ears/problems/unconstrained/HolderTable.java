package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/holder.html
http://benchmarkfcns.xyz/benchmarkfcns/holdertablefcn.html
 */

public class HolderTable extends Problem {
    public HolderTable() {
        super(2, 0, 4);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Holder Table";

        optimum[0] = new double[]{8.05502, 9.66459};
        optimum[1] = new double[]{8.05502, -9.66459};
        optimum[2] = new double[]{-8.05502, 9.66459};
        optimum[3] = new double[]{-8.05502, -9.66459};
    }

    @Override
    public double eval(double[] x) {
        double fact1, fact2;
        fact1 = sin(x[0]) * cos(x[1]);
        fact2 = exp(abs(1 - sqrt(pow(x[0], 2) + pow(x[1], 2)) / PI));
        return -abs(fact1 * fact2);
    }

    @Override
    public double getGlobalOptimum() {
        return -19.2085;
    }
}
