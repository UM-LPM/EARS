package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

/*
http://infinity77.net/global_optimization/test_functions_nd_R.html#go_benchmark.RotatedEllipse02
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/127-rotated-ellipse-function-no-2
 */
public class RotatedEllipse2 extends DoubleProblem {

    public RotatedEllipse2() {
        super("RotatedEllipse2", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 500.0));
    }

    @Override
    public double eval(double[] x) {
        return pow(x[0], 2) - x[0] * x[1] + pow(x[1], 2);
    }
}