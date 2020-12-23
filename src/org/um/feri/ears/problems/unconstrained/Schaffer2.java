package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

/*
http://benchmarkfcns.xyz/benchmarkfcns/schaffern2fcn.html
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schaffer02
 */
public class Schaffer2 extends Problem {
    public Schaffer2() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Schaffer2";
    }

    @Override
    public double eval(double[] x) {
        return 0.5 + (pow(sin(pow(pow(x[0], 2) - pow(x[1], 2), 2)), 2) - 0.5) /
                (1 + 0.001 * pow((pow(x[0], 2) + pow(x[1], 2)), 2));
    }
}