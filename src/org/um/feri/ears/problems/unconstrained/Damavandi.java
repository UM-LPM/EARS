package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_D.html#go_benchmark.Damavandi
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/120-damavandi-s-function
 */
public class Damavandi extends Problem {

    public Damavandi() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 14.0));
        name = "Damavandi";
        Arrays.fill(optimum[0], 2.0);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double frac, numerator, denominator;
        numerator = sin(PI * (x[0] - 2)) * sin(PI * (x[1] - 2));
        denominator = (pow(PI, 2) * (x[0] - 2) * (x[1] - 2));
        if (denominator == 0)
            frac = 1;
        else
            frac = numerator / denominator;
        fitness = (1 - pow(abs(frac), 5)) * (2 + pow(x[0] - 7, 2) + 2 * pow(x[1] - 7, 2));

        // original equation causes NaN - division by 0
        /*result = (1 - pow(abs((sin(PI * (x[0] - 2)) * sin(PI * (x[1] - 2))) / (pow(PI, 2) * (x[0] - 2) * (x[1] - 2))), 5)) *
                (2 + pow(x[0] - 7, 2) + 2 * pow(x[1] - 7, 2));*/
        return fitness;
    }
}