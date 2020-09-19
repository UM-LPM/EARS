package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_B.html#go_benchmark.Bohachevsky
http://benchmarkfcns.xyz/benchmarkfcns/bohachevskyn1fcn.html
 */
public class Bohachevsky1 extends Problem {


    public Bohachevsky1() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Bohachevsky1";
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(x[0], 2)
                + 2 * pow(x[1], 2)
                - 0.3 * cos(3 * PI * x[0])
                - 0.4 * cos(4 * PI * x[1])
                + 0.7;
        return fitness;
    }
}
