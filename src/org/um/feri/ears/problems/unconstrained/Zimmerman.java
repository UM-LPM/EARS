package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;

import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.signum;

/*
http://infinity77.net/global_optimization/test_functions_nd_Z.html#go_benchmark.Zimmerman
 */
public class Zimmerman extends DoubleProblem {

    public Zimmerman() {
        super("Zimmerman", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));

        //optimum[0] = new double[]{7.0, 2.0};
    }

    @Override
    public double eval(double[] x) {
        double p1 = zh1(x);
        double p2 = zp(zh2(x)) * signum(zh2(x));
        double p3 = zp(zh3(x)) * signum(zh3(x));
        double p4 = zp(-x[0]) * signum(x[0]);
        double p5 = zp(-x[1]) * signum(x[1]);

        return Util.max(p1, p2, p3, p4, p5);
    }

    private double zh1(double[] x) {
        return 9 - x[0] - x[1];
    }

    private double zh2(double[] x) {
        return pow(x[0] - 3, 2) + pow(x[1] - 2, 2);
    }

    private double zh3(double[] x) {
        return x[0] * x[1] - 14;
    }

    private double zp(double t) {
        return 100 * (1 + t);
    }

}