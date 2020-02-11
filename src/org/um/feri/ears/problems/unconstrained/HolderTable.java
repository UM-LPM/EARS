package org.um.feri.ears.problems.unconstrained;

import org.apache.commons.lang.ArrayUtils;
import org.um.feri.ears.problems.Problem;
import java.util.ArrayList;
import java.util.Collections;

//https://www.sfu.ca/~ssurjano/holder.html

public class HolderTable extends Problem {
    public HolderTable() {
        super(2,0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "HolderTable";
    }

    @Override
    public double eval(double[] x) {

        double x1 = x[0];
        double x2 = x[1];
        double fitness = 0, fact1 = 0, fact2 = 0;

        fact1 = Math.sin(x1) * Math.cos(x2);
        fact2 = Math.exp(Math.abs(1 - Math.sqrt(Math.pow(x1, 2) + Math.pow(x2, 2)) / Math.PI));

        fitness = -Math.abs(fact1 * fact2);

        return fitness;
    }

    public double getOptimumEval() {
        return -19.2085;
    }

    public double eval(Double[] ds) {
        return eval(ArrayUtils.toPrimitive(ds));
    }
}
