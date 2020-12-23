package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schaffer03
http://benchmarkfcns.xyz/benchmarkfcns/schaffern3fcn.html
 */
public class Schaffer3 extends Problem {
    public Schaffer3() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Schaffer3";

        optimum[0] = new double[]{0, 1.253115};
    }

    @Override
    public double eval(double[] x) {
        return 0.5 + (pow(sin(cos(abs(pow(x[0], 2) - pow(x[1], 2)))), 2) - 0.5) /
                (1 + 0.001 * pow((pow(x[0], 2) + pow(x[1], 2)), 2));
    }

    @Override
    public double getGlobalOptimum() {
        return 0.0012301324758943188;
    }
}