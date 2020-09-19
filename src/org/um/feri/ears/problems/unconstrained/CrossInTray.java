package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/crossit.html
http://infinity77.net/global_optimization/test_functions_nd_C.html#go_benchmark.CrossInTray
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/44-cross-in-tray-function
http://benchmarkfcns.xyz/benchmarkfcns/crossintrayfcn.html
 */

public class CrossInTray extends Problem {

    public CrossInTray() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -15.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 15.0));
        name = "CrossInTray";

        Arrays.fill(optimum[0], 1.349406608602084);
    }

    @Override
    public double eval(double[] x) {

        double fitness = 0, fact1 = 0, fact2 = 0;
        double x1 = x[0];
        double x2 = x[1];
        fact1 = sin(x1) * sin(x2);
        fact2 = exp(abs(100 - sqrt(pow(x1, 2) + pow(x2, 2)) / PI));
        fitness = -0.0001 * pow((abs(fact1 * fact2) + 1), 0.1);
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -2.062611870822739;
    }
}
