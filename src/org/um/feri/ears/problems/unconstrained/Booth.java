package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/booth.html
http://benchmarkfcns.xyz/benchmarkfcns/boothfcn.html
*/

public class Booth extends Problem {

    public Booth() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Booth";

        optimum[0][0] = 1.0;
        optimum[0][1] = 3.0;
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(x[0] + 2 * x[1] - 7, 2)
                + pow(2 * x[0] + x[1] - 5, 2);
        return fitness;
    }
}
