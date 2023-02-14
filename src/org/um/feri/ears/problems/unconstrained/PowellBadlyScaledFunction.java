package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.exp;
import static java.lang.Math.pow;

/*
http://al-roomi.org/benchmarks/unconstrained/2-dimensions/65-powell-s-badly-scaled-function
 */
public class PowellBadlyScaledFunction extends DoubleProblem {

    public PowellBadlyScaledFunction() {
        super("Powellbadlyscaledfunction", 2, 1, 1, 0);

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -50.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 50.0));

        decisionSpaceOptima[0][0] = 1.09815933e-5;
        decisionSpaceOptima[0][1] = 9.106146738;
    }

    @Override
    public double eval(double[] x) {
        return pow(10. * x[0] * x[1] - 1, 2) + pow(exp(-x[0]) + exp(-x[1]) - 1.0001, 2);
    }
}
