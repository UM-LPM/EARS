package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://benchmarkfcns.xyz/benchmarkfcns/xinsheyangn4fcn.html
http://infinity77.net/global_optimization/test_functions_nd_X.html#go_benchmark.XinSheYang04
 */
public class XinSheYang4 extends DoubleProblem {

    public XinSheYang4() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "XinSheYang4";
        objectiveSpaceOptima[0] = -1.0;
    }

    @Override
    public double eval(double[] x) {
        double sum1 = 0, sum2 = 0, sum3 = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum1 += pow(sin(x[i]), 2);
            sum2 += pow(x[i], 2);
            sum3 += pow(sin(sqrt(abs(x[i]))), 2);
        }
        return (sum1 - exp(-sum2)) * exp(-sum3);
    }
}