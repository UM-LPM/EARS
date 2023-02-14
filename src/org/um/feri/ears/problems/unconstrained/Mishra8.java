package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
/*
http://infinity77.net/global_optimization/test_functions_nd_D.html#go_benchmark.Decanomial
https://al-roomi.org/benchmarks/unconstrained/2-dimensions/49-mishra-s-function-no-8-or-decanomial-function
 */

public class Mishra8 extends DoubleProblem {

    public Mishra8() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Mishra8"; //also known as Decanomial

        decisionSpaceOptima[0] = new double[]{2, -3.0};
    }

    @Override
    public double eval(double[] x) {
        double fp, sp;
        // First polynomial
        fp = pow(x[0], 10) - 20 * pow(x[0], 9) + 180 * pow(x[0], 8) - 960 * pow(x[0], 7) + 3360 * pow(x[0], 6) - 8064 * pow(x[0], 5) + 13340 * pow(x[0], 4) - 15360 * pow(x[0], 3) + 11520 * pow(x[0], 2) - 5120 * x[0] + 2624;
        // Second polynomial
        sp = pow(x[1], 4) + 12 * pow(x[1], 3) + 54 * pow(x[1], 2) + 108 * x[1] + 81;

        return 0.001 * pow(abs(fp) + abs(sp), 2);
    }
}