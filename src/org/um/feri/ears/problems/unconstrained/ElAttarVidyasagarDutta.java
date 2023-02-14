package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/121-el-attar-vidyasagar-dutta-s-function
http://infinity77.net/global_optimization/test_functions_nd_E.html#go_benchmark.ElAttarVidyasagarDutta
 */
public class ElAttarVidyasagarDutta extends DoubleProblem {

    public ElAttarVidyasagarDutta() {
        super("ElAttarVidyasagarDutta", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));

        decisionSpaceOptima[0] = new double[]{3.4091868222, -2.1714330361};
        objectiveSpaceOptima[0] = 1.712780354862198;
    }

    @Override
    public double eval(double[] x) {
        return pow(pow(x[0], 2) + x[1] - 10, 2) + pow(x[0] + pow(x[1], 2) - 7, 2) + pow(pow(x[0], 2) + pow(x[1], 3) - 1, 2);
    }
}