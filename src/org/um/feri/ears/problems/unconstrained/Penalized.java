package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Penalty01
 */
public class Penalized extends Problem {

    public Penalized(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -50.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 50.0));
        name = "Penalized"; // also known as Penalty1

        Arrays.fill(optimum[0], -1);
    }

    @Override
    public double eval(double[] x) {
        double sum1 = 0, sum2 = 0, k2, k1;
        double[] y = new double[numberOfDimensions];
        for (int i = 0; i < numberOfDimensions; i++) {
            y[i] = 1 + (1.0 / 4.0) * (x[i] + 1);
        }
        k1 = 10 * pow(sin(PI * y[0]), 2);
        for (int i = 0; i < numberOfDimensions - 1; i++) {
            sum1 += pow(y[i] - 1, 2) * (1 + 10 * pow(sin(PI * y[i + 1]), 2));
        }
        k2 = pow(y[numberOfDimensions - 1] - 1, 2);
        for (int i = 0; i < numberOfDimensions; i++) {
            sum2 += u(x[i], 10, 100, 4);
        }
        return PI / numberOfDimensions * (k1 + sum1 + k2) + sum2;
    }

    private double u(double x, double a, double k, double m) {
        if (x > a) return k * pow(x - a, m);
        else if (x < a && x > -1 * a) return 0;
        else return k * pow(-1 * x - a, m);
    }
}
