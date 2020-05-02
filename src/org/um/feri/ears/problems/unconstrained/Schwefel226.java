package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import static java.lang.Math.*;

/*
http://al-roomi.org/benchmarks/unconstrained/n-dimensions/176-generalized-schwefel-s-problem-2-26
 http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schwefel26

*/
public class Schwefel226 extends Problem {

    public Schwefel226(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "Schwefel226";

        Arrays.fill(optimum[0], 420.968746);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += -x[i] * sin(sqrt(abs(x[i])));
        }
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -418.9828872724337998079 * numberOfDimensions;
    }
}
