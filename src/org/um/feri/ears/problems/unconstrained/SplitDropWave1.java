package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

public class SplitDropWave1 extends Problem {
    public SplitDropWave1() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -3.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 3.0));
        name = "SplitDropWave1";
    }

    @Override
    public double eval(double[] x) {
        return cos(pow(x[0], 2) + pow(x[1], 2)) + 2 * exp(-10 * pow(x[1], 2));
    }
}