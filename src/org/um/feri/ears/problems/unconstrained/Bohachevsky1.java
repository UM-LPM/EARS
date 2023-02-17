package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_B.html#go_benchmark.Bohachevsky
http://benchmarkfcns.xyz/benchmarkfcns/bohachevskyn1fcn.html
 */
public class Bohachevsky1 extends DoubleProblem {


    public Bohachevsky1() {
        super("Bohachevsky1", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0], 2)
                + 2 * pow(x[1], 2)
                - 0.3 * cos(3 * PI * x[0])
                - 0.4 * cos(4 * PI * x[1])
                + 0.7;
    }
}
