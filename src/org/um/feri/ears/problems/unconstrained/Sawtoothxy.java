package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/66-sawtoothxy-function
 */
public class Sawtoothxy extends DoubleProblem {

    public Sawtoothxy() {
        super(2, 1, 1, 0);
        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -20.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 20.0));
        name = "Sawtoothxy";
    }

    @Override
    public double eval(double[] x) {
        double r = sqrt(pow(x[0], 2) + pow(x[1], 2));
        double t = atan2(x[1], x[0]);
        double h = cos(2 * t - 1.0 / 2.0) / 2 + cos(t) + 2;
        double g = (sin(r) - sin(2 * r) / 2 + sin(3 * r) / 3 - sin(4 * r) / 4 + 4) * pow(r, 2) / (r + 1);
        return g * h;
    }
}