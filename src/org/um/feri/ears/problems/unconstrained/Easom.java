package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/easom.html
http://benchmarkfcns.xyz/benchmarkfcns/easomfcn.html

Different equation at: http://infinity77.net/global_optimization/test_functions_nd_E.html#go_benchmark.Easom

*/
public class Easom extends Problem {

    public Easom() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Easom";

        Arrays.fill(optimum[0], PI);
    }

    @Override
    public double eval(double[] x) {
        return -1 * cos(x[0]) * cos(x[1]) * exp(-1 * pow(x[0] - PI, 2) - pow(x[1] - PI, 2));
    }

    @Override
    public double getGlobalOptimum() {
        return -1.0;
    }
}
