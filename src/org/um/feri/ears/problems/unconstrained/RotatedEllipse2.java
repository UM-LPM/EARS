package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/*
http://infinity77.net/global_optimization/test_functions_nd_R.html#go_benchmark.RotatedEllipse02
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/127-rotated-ellipse-function-no-2
 */
public class RotatedEllipse2 extends Problem {

    public RotatedEllipse2() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "RotatedEllipse2";
    }

    @Override
    public double eval(double[] x) {
        double fitness = pow(x[0], 2) - x[0] * x[1] + pow(x[1], 2);
        return fitness;
    }
}