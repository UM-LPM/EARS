package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import static java.lang.Math.*;

/**
 * http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page295.htm
 * https://www.sfu.ca/~ssurjano/ackley.html
 * https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/227-ackley-s-function-no-1-or-ackley-s-path-function
 * http://infinity77.net/global_optimization/test_functions_nd_A.html#go_benchmark.Ackley
 */

public class Ackley1 extends Problem {

    public Ackley1(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -32.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 32.0));
        name = "Ackley1";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double a = 20.0, b = 0.2, c = 2 * PI;
        double sum1 = 0, sum2 = 0;

        for (int i = 0; i < numberOfDimensions; i++) {
            sum1 += pow(x[i], 2);
            sum2 += Math.cos(2 * Math.PI * x[i]);
        }
        fitness = -a * Math.exp(-b * sqrt(1.0 / numberOfDimensions * sum1)) - exp(1.0 / numberOfDimensions * sum2) + a + E;
        return fitness;
    }
}
