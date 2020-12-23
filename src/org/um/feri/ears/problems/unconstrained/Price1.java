package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/28-becker-lago-s-function
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Price01
 */
public class Price1 extends Problem {

    public Price1() {
        super(2, 0, 4);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "Price1"; // also known as Becker-Lago's Function

        optimum[0] = new double[]{5.0, 5.0};
        optimum[1] = new double[]{5.0, -5.0};
        optimum[2] = new double[]{-5.0, 5.0};
        optimum[3] = new double[]{-5.0, -5.0};
    }

    @Override
    public double eval(double[] x) {
        return pow(abs(x[0]) - 5, 2) + pow(abs(x[1]) - 5, 2);
    }
}