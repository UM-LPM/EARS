package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/zakharov.html
http://infinity77.net/global_optimization/test_functions_nd_Z.html#go_benchmark.Zacharov
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/196-zakharov-s-function
http://benchmarkfcns.xyz/benchmarkfcns/zakharov.html
 */

public class Zakharov extends Problem {

    public Zakharov(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Zakharov";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum1 = 0, sum2 = 0, sum3 = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            sum1 += pow(x[i], 2);
        }
        for (int i = 0; i < numberOfDimensions; i++) {
            sum2 += 0.5 * (i + 1) * x[i];
        }
        sum2 = pow(sum2, 2);
        for (int i = 0; i < numberOfDimensions; i++) {
            sum3 += 0.5 * (i + 1) * x[i];
        }
        sum3 = pow(sum3, 4);
        fitness = sum1 + sum2 + sum3;
        return fitness;
    }
}
