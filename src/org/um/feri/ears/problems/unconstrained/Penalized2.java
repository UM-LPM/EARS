package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Penalty02
 */
public class Penalized2 extends Problem {

    public Penalized2(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -50.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 50.0));
        name = "Penalized2"; // also known as Penalty2

        Arrays.fill(optimum[0], 1);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double v1 = 0, sum1 = 0, v3 = 0, sum2 = 0;
        v1 = pow(sin(PI * x[0]), 2);
        for (int i = 0; i < numberOfDimensions - 1; i++) {
            sum1 += pow(x[i] - 1, 2) * (1 + pow(sin(3 * PI * x[i + 1]), 2));
        }
        v3 = pow(x[numberOfDimensions - 1] - 1, 2) * (1 + pow(sin(1 * PI * x[numberOfDimensions - 1]), 2));
        for (int i = 0; i < numberOfDimensions; i++) {
            sum2 += u(x[i], 5, 100, 4);
        }
        fitness = PI / numberOfDimensions * (v1 + sum1 + v3) + sum2;
        return fitness;
    }

    private double u(double x, double a, double k, double m) {
        if (x > a) return k * pow(x - a, m);
        else if (x < a && x > -1 * a) return 0;
        else return k * pow(-1 * x - a, m);
    }
}
