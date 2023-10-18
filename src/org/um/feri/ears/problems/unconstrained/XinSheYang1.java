package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;

/*
http://infinity77.net/global_optimization/test_functions_nd_X.html#go_benchmark.XinSheYang01
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Stochastic
http://benchmarkfcns.xyz/benchmarkfcns/xinsheyangn1fcn.html
 */
public class XinSheYang1 extends DoubleProblem {

    public XinSheYang1() {
        super("XinSheYang1", 2, 1, 1, 0);
        // also known as Stochastic Function
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));

        for (int i = 0; i < numberOfDimensions; i++) {
            decisionSpaceOptima[0][i] = 1.0 / (i + 1);
        }
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += RNG.nextDouble() * abs(x[i] - 1.0 / (i + 1));
        }
        return fitness;
    }
}