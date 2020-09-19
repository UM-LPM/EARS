package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;
import static java.lang.Math.*;

/*
https://www.sfu.ca/~ssurjano/michal.html
 */

public class Michalewicz5 extends Problem {

    public Michalewicz5() {
        super(5, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, PI));
        name = "Michalewicz5";
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        int m = 10;
        for (int i = 0; i < numberOfDimensions; i++) {
            fitness += sin(x[i]) * pow(sin((i + 1) * x[i] * x[i] / PI), 2 * m);
        }
        return -fitness;
    }

    @Override
    public double getGlobalOptimum() {
        return -4.68765817;
    }
}
