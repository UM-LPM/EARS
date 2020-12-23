package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/184-salomon-s-function
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Salomon
http://benchmarkfcns.xyz/benchmarkfcns/salomonfcn.html
 */
public class Salomon extends Problem {

    public Salomon() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Salomon";
    }

    @Override
    public double eval(double[] x) {
        double norm = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            norm += pow(x[i], 2);
        }
        norm = sqrt(norm);
        return 1 - cos(2 * PI * norm) + 0.1 * norm;
    }
}