package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/easom.html
http://benchmarkfcns.xyz/benchmarkfcns/easomfcn.html

Different equation at: http://infinity77.net/global_optimization/test_functions_nd_E.html#go_benchmark.Easom

*/
public class Easom extends DoubleProblem {

    public Easom() {
        super("Easom", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));

        Arrays.fill(decisionSpaceOptima[0], PI);
        objectiveSpaceOptima[0] = -1.0;
    }

    @Override
    public double eval(double[] x) {
        return -1 * cos(x[0]) * cos(x[1]) * exp(-1 * pow(x[0] - PI, 2) - pow(x[1] - PI, 2));
    }
}
