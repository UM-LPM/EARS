package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.cos;
import static java.lang.Math.sqrt;

/*
https://www.sfu.ca/~ssurjano/griewank.html
http://benchmarkfcns.xyz/benchmarkfcns/griewankfcn.html
*/

public class Griewank extends DoubleProblem {

    public Griewank(int d) {
        super("Griewank", d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -600.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 600.0));
    }

    @Override
    public double eval(double[] x) {
        double sum = 0;
        double prod = 1;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum += x[i] * x[i];
            prod = prod * cos(x[i] / sqrt(i + 1));
        }
        return (1.0 / 4000.0) * sum - prod + 1;
    }
}
