package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/121-el-attar-vidyasagar-dutta-s-function
http://infinity77.net/global_optimization/test_functions_nd_E.html#go_benchmark.ElAttarVidyasagarDutta
 */
public class ElAttarVidyasagarDutta extends Problem {

    public ElAttarVidyasagarDutta() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "ElAttarVidyasagarDutta";

        optimum[0] = new double[]{3.4091868222, -2.1714330361};
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(pow(x[0], 2) + x[1] - 10, 2) + pow(x[0] + pow(x[1], 2) - 7, 2) + pow(pow(x[0], 2) + pow(x[1], 3) - 1, 2);
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return 1.712780354862198;
    }
}