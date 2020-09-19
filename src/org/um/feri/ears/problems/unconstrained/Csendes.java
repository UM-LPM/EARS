package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;
import java.util.ArrayList;
import java.util.Collections;
import static java.lang.Math.*;

/*
https://www.al-roomi.org/benchmarks/unconstrained/n-dimensions/167-ex3-csendes-or-infinity-function
http://infinity77.net/global_optimization/test_functions_nd_C.html#go_benchmark.Csendes
 */
public class Csendes extends Problem {

    public Csendes() {
        super(2, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -1.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.0));
        name = "Csendes";
    }

    @Override
    public double eval(double[] x) {
        double result = 0;
        for (int i = 0; i < numberOfDimensions; i++) {
            result += pow(x[i],6) * (2 + sin(1.0 / x[i]));
        }
        return result;
    }
}