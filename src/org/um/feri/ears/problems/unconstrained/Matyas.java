package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/matya.html
http://benchmarkfcns.xyz/benchmarkfcns/matyasfcn.html
 */

public class Matyas extends Problem {

    public Matyas() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Matyas";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0.26 * (pow(x[0], 2) + pow(x[1], 2)) - 0.48 * x[0] * x[1];
        return fitness;
    }
}
