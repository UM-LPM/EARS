package org.um.feri.ears.problems.unconstrained;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * https://www.sfu.ca/~ssurjano/drop.html
 */
public class DropWave extends Problem {

    public DropWave() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.12));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.12));
        name = "Drop-Wave";
    }

    public double eval(double x[]) {
        double x1 = x[0];
        double x2 = x[1];
        return -((1.0 + Math.cos(12.0 * Math.sqrt((x1 * x1) + (x2 * x2)))) / ((((x1 * x1) + (x2 * x2)) / 2.0) + 2.0));
    }

    public double getOptimumEval() {
        return -1.0;
    }

    @Override
    public double eval(Double[] ds) {
        return eval(ArrayUtils.toPrimitive(ds));
    }
}
