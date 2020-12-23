package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.pow;

public class MartinAndGaddy extends Problem {
    public MartinAndGaddy() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "MartinAndGaddy";

        Arrays.fill(optimum[0], 5.0);
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0] - x[1], 2) + pow((x[0] + x[1] - 10.) / 3., 2);
    }
}
