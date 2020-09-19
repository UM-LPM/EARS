package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;
import static java.lang.Math.exp;

/*
http://benchmarkfcns.xyz/benchmarkfcns/xinsheyangn4fcn.html
http://infinity77.net/global_optimization/test_functions_nd_X.html#go_benchmark.XinSheYang04
 */
public class XinSheYang4 extends Problem {

    public XinSheYang4() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "XinSheYang4";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum1 = 0, sum2 = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum1 += pow(x[i], 2);
            sum2 += pow(sin(sqrt(abs(x[i]))), 2);
        }
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness = (pow(sin(x[i]), 2) - exp(-sum1)) * exp(-sum2);
        }
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -1.0;
    }
}