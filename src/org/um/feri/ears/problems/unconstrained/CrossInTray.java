package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


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

public class CrossInTray extends DoubleProblem {

    public CrossInTray() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -15.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 15.0));
        name = "CrossInTray";

        Arrays.fill(decisionSpaceOptima[0], 1.349406608602084);
        objectiveSpaceOptima[0] = -2.062611870822739;
    }

    @Override
    public double eval(double[] x) {
        double fact1, fact2;
        fact1 = sin(x[0]) * sin(x[1]);
        fact2 = exp(abs(100 - sqrt(pow(x[0], 2) + pow(x[1], 2)) / PI));
        return -0.0001 * pow((abs(fact1 * fact2) + 1), 0.1);
    }
}
