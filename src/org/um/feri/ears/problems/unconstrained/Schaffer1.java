package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

/*
http://benchmarkfcns.xyz/benchmarkfcns/schaffern1fcn.html
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schaffer01
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/96-modified-schaffer-s-function-no-1
 */
public class Schaffer1 extends DoubleProblem {
    public Schaffer1() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Schaffer1";
    }

    @Override
    public double eval(double[] x) {
        return 0.5 + (pow(sin(pow(pow(x[0], 2) + pow(x[1], 2), 2)), 2) - 0.5) /
                (1 + 0.001 * pow((pow(x[0], 2) + pow(x[1], 2)), 2));
    }
}
