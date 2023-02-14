package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_C.html#go_benchmark.CarromTable
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/32-carrom-table-function
 */
public class CarromTable extends DoubleProblem {

    public CarromTable() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "CarromTable";

        Arrays.fill(decisionSpaceOptima[0], 9.646157266348881);
        objectiveSpaceOptima[0] = -24.15681551650653;
    }

    @Override
    public double eval(double[] x) {
        return (-1.0 / 30.0) * exp(2 * abs(1 - (sqrt(pow(x[0], 2) + pow(x[1], 2)) / PI))) * pow(cos(x[0]), 2) * pow(cos(x[1]), 2);
    }
}