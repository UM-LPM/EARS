package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_R.html#go_benchmark.RotatedEllipse01
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/126-rotated-ellipse-function-no-1
 */
public class RotatedEllipse1 extends Problem {

    public RotatedEllipse1() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "RotatedEllipse1";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 7 * pow(x[0], 2) - 6 * sqrt(3) * x[0] * x[1] + 13 * pow(x[1], 2);
        return fitness;
    }
}