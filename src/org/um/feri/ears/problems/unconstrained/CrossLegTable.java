package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_C.html#go_benchmark.CrossLegTable
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/45-cross-leg-table-function
 */
public class CrossLegTable extends DoubleProblem {

    public CrossLegTable() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "CrossLegTable";
        objectiveSpaceOptima[0] = -1.0;
    }

    @Override
    public double eval(double[] x) {
        return -1 / (pow(abs(exp(abs(100 - (sqrt(pow(x[0], 2) + pow(x[1], 2)) / PI))) * sin(x[0]) * sin(x[1])) + 1, 0.1));
    }
}