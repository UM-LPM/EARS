package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
import static java.lang.Math.PI;
/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/31-camel-function
 */
public class Camel extends Problem {

    public Camel() {
        super(2, 0, 2);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.0));
        name = "Camel";

        optimum[0][0] = 1.5;
        optimum[0][1] = 0.0;

        optimum[1][0] = -1.5;
        optimum[1][1] = 0.0;
    }

    @Override
    public double eval(double[] x) {
        return -((-pow(x[0], 4) + 4.5 * pow(x[0], 2) + 2) / (exp(2 * pow(x[1], 2))));
    }

    @Override
    public double getGlobalOptimum() {
        return -7.0625;
    }
}