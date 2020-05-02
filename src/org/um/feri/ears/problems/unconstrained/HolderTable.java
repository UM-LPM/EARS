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
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Holder Table";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0, fact1 = 0, fact2 = 0;
        fact1 = sin(x[0]) * cos(x[1]);
        fact2 = exp(abs(1 - sqrt(pow(x[0], 2) + pow(x[1], 2)) / PI));
        fitness = -abs(fact1 * fact2);
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -19.2085;
    }
}
