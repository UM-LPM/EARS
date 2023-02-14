package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schaffer03
http://benchmarkfcns.xyz/benchmarkfcns/schaffern3fcn.html
 */
public class Schaffer3 extends DoubleProblem {
    public Schaffer3() {
        super("Schaffer3", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));

        decisionSpaceOptima[0] = new double[]{0, 1.253115};
        objectiveSpaceOptima[0] = 0.0012301324758943188;
    }

    @Override
    public double eval(double[] x) {
        return 0.5 + (pow(sin(cos(abs(pow(x[0], 2) - pow(x[1], 2)))), 2) - 0.5) /
                (1 + 0.001 * pow((pow(x[0], 2) + pow(x[1], 2)), 2));
    }
}