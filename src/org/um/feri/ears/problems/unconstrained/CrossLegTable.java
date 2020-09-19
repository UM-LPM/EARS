package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_C.html#go_benchmark.CrossLegTable
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/45-cross-leg-table-function
 */
public class CrossLegTable extends Problem {

    public CrossLegTable() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "CrossLegTable";
    }

    @Override
    public double eval(double[] x) {
        double fitness = -1 / (pow(abs(exp(abs(100 - (sqrt(pow(x[0], 2) + pow(x[1], 2)) / PI))) * sin(x[0]) * sin(x[1])) + 1, 0.1));
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -1.0;
    }
}