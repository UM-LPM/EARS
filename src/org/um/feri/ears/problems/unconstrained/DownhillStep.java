package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/2-dimensions/114-downhill-step-function
 */
public class DownhillStep extends Problem {

    public DownhillStep() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "DownhillStep";
    }

    @Override
    public double eval(double[] x) {
        double fitness = floor(10.0 * (10.0 - exp(-pow(x[0], 2) - 3 * pow(x[1], 2)))) / 10.0;
        return fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return 9.0;
    }
}