package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schaffer04
http://benchmarkfcns.xyz/benchmarkfcns/schaffern4fcn.html
 */
public class Schaffer4 extends Problem {
    public Schaffer4() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Schaffer4";

        optimum[0] = new double[]{0, 1.253115};
    }

    @Override
    public double eval(double[] x) {
        return 0.5 + (pow(cos(sin(pow(x[0], 2) - pow(x[1], 2))), 2) - 0.5) /
                (1 + 0.001 * pow((pow(x[0], 2) + pow(x[1], 2)), 2));
    }

    @Override
    public double getGlobalOptimum() {
        return 0.29243850703298857;
    }
}