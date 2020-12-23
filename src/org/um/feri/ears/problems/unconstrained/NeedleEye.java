package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;

/*
http://infinity77.net/global_optimization/test_functions_nd_N.html#go_benchmark.NeedleEye
https://al-roomi.org/benchmarks/unconstrained/n-dimensions/183-needle-eye-function
 */
public class NeedleEye extends Problem {

    public NeedleEye() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "NeedleEye";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double eye = 0.0001;
        boolean inEye = false;
        for (int i = 0; i < numberOfDimensions; i++) {
            if (abs(x[i]) <= eye) {
                inEye = true;
                break;
            }
        }

        if (inEye) {
            fitness = 1.0;
        } else {
            for (int i = 0; i < numberOfDimensions; i++) {
                fitness += (100 + abs(x[i])) * ((abs(x[i]) > eye) ? 1 : 0);
            }
        }
        return fitness;
    }
}