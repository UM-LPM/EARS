package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_D.html#go_benchmark.Damavandi
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/120-damavandi-s-function
 */
public class Damavandi extends DoubleProblem {

    public Damavandi() {
        super("Damavandi", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 14.0));
        Arrays.fill(decisionSpaceOptima[0], 2.0);
    }

    @Override
    public double eval(double[] x) {
        double frac, numerator, denominator;
        numerator = sin(PI * (x[0] - 2)) * sin(PI * (x[1] - 2));
        denominator = (pow(PI, 2) * (x[0] - 2) * (x[1] - 2));
        if (denominator == 0)
            frac = 1;
        else
            frac = numerator / denominator;
        // original equation causes NaN - division by 0
        /*result = (1 - pow(abs((sin(PI * (x[0] - 2)) * sin(PI * (x[1] - 2))) / (pow(PI, 2) * (x[0] - 2) * (x[1] - 2))), 5)) *
                (2 + pow(x[0] - 7, 2) + 2 * pow(x[1] - 7, 2));*/
        return (1 - pow(abs(frac), 5)) * (2 + pow(x[0] - 7, 2) + 2 * pow(x[1] - 7, 2));
    }
}