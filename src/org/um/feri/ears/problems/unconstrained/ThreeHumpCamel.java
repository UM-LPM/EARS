package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/camel3.html
http://benchmarkfcns.xyz/benchmarkfcns/threehumpcamelfcn.html
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/67-three-hump-camel-back-function
 */

public class ThreeHumpCamel extends Problem {

    public ThreeHumpCamel() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
        name = "Three Hump Camel";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 2 * pow(x[0], 2) + -1.05 * pow(x[0], 4) + pow(x[0], 6) / 6.0 + x[0] * x[1] + pow(x[1], 2);
        return fitness;
    }
}