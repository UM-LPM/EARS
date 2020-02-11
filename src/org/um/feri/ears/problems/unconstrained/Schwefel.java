package org.um.feri.ears.problems.unconstrained;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//https://www.sfu.ca/~ssurjano/schwef.html

public class Schwefel extends Problem {

    public Schwefel(int d) {
        super(d,0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "Schwefel";
        Arrays.fill(optimum[0], 420.968746);
    }

    @Override
    public double eval(double[] x) {
        double v = 0;
        for (int i = 0; i < numberOfDimensions; i++){
            v += x[i]*Math.sin(Math.sqrt(Math.abs(x[i])));
        }
        return 418.9829 * numberOfDimensions - v;
    }

    @Override
    public double eval(Double[] ds) {
        return eval(ArrayUtils.toPrimitive(ds));
    }
}
