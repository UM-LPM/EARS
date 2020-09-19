package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import static java.lang.Math.*;

/*
http://benchmarkfcns.xyz/benchmarkfcns/schaffern1fcn.html
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schaffer01
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/96-modified-schaffer-s-function-no-1
 */
public class Schaffer1 extends Problem {
    public Schaffer1() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Schaffer1";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0.5 + (pow(sin(pow(pow(x[0], 2) + pow(x[1], 2), 2)), 2) - 0.5) /
                (1 + 0.001 * pow((pow(x[0], 2) + pow(x[1], 2)), 2));
        return fitness;
    }
}
