package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/130-ursem-function-no-3
http://infinity77.net/global_optimization/test_functions_nd_U.html#go_benchmark.Ursem03
 */
public class Ursem3 extends DoubleProblem {

    public Ursem3() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 0.0));
        name = "Ursem3";

        lowerLimit.set(0, -2.0);
        upperLimit.set(0, 2.0);

        lowerLimit.set(1, -1.5);
        upperLimit.set(1, 1.5);
        objectiveSpaceOptima[0] = -2.5;
    }

    @Override
    public double eval(double[] x) {
        return -sin(2.2 * PI * x[0] + 0.5 * PI) * ((3 - abs(x[0])) / 2) * ((2 - abs(x[1])) / 2) -
                sin(0.5 * PI * pow(x[1], 2) + 0.5 * PI) * ((2 - abs(x[0])) / 2) * ((2 - abs(x[1])) / 2);
    }
}