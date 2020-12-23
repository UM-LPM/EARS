package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
http://infinity77.net/global_optimization/test_functions_nd_P.html#go_benchmark.Parsopoulos
https://al-roomi.org/benchmarks/unconstrained/2-dimensions/252-parsopoulos-function
 */
public class Parsopoulos extends Problem {

    public Parsopoulos() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
        name = "Parsopoulos";
    }

    @Override
    public double eval(double[] x) {
        return pow(cos(x[0]), 2) * pow(sin(x[1]), 2);
    }
}