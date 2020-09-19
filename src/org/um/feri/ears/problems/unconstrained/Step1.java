package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/192-step-function-no-1
http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Step
 */
public class Step1 extends Problem {
    public Step1(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
        name = "Step1";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += pow(floor(x[i] + 0.5), 2);
        }
        return fitness;
    }
}
