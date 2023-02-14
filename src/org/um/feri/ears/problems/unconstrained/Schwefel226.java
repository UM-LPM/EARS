package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://al-roomi.org/benchmarks/unconstrained/n-dimensions/176-generalized-schwefel-s-problem-2-26
 http://infinity77.net/global_optimization/test_functions_nd_S.html#go_benchmark.Schwefel26

*/
public class Schwefel226 extends DoubleProblem {

    public Schwefel226(int d) {
        super(d, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -500.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 500.0));
        name = "Schwefel226";

        Arrays.fill(decisionSpaceOptima[0], 420.968746);
        objectiveSpaceOptima[0] = -418.9828872724337998079 * numberOfDimensions;
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += -x[i] * sin(sqrt(abs(x[i])));
        }
        return fitness;
    }
}
