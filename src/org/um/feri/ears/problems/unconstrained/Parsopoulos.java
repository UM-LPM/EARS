package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Parsopoulos
https://al-roomi.org/benchmarks/unconstrained/2-dimensions/252-parsopoulos-function
 */
public class Parsopoulos extends DoubleProblem {

    public Parsopoulos() {
        super("Parsopoulos", 2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));
    }

    @Override
    public double eval(double[] x) {
        return pow(cos(x[0]), 2) * pow(sin(x[1]), 2);
    }
}