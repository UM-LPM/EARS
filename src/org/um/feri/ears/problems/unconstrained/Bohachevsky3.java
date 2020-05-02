package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import static java.lang.Math.*;

public class Bohachevsky3 extends Problem {


    public Bohachevsky3() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Bohachevsky3";
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(x[0], 2)
                + 2 * pow(x[1], 2)
                - 0.3 * cos(3 * PI * x[0] + 4 * PI * x[1])
                + 0.3;
        return fitness;
    }
}
