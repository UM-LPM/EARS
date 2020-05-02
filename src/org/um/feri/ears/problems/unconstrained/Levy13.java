package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/levy13.html
http://benchmarkfcns.xyz/benchmarkfcns/levin13fcn.html
 */

public class Levy13 extends Problem {

    public Levy13() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Levy13";

        Arrays.fill(optimum[0], 1);
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(sin(3 * PI * x[0]), 2) + pow(x[0] - 1, 2) *
                (1 + pow(Math.sin(3 * PI * x[1]), 2)) + pow(x[1] - 1, 2) *
                (1 + pow(Math.sin(2 * PI * x[1]), 2));
        return fitness;
    }

}