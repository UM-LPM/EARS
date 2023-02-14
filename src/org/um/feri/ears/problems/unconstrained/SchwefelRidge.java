package org.um.feri.ears.problems.unconstrained;

import org.um.feri.ears.problems.DoubleProblem;


import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;

public class SchwefelRidge extends DoubleProblem {
    // Rosenbrock
    //http://www.geatbx.com/docu/fcnindex-01.html#P86_3059
    public SchwefelRidge(int d) {
        super("SchwefelRigle(" + d + ")", d , 1, 1, 0);

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -64.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 64.0));
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        double sum = 0;
        for (int i = 0; i < (numberOfDimensions); i++) {
            for (int j = 0; j <= i; j++) {
                sum += x[j];
            }
            fitness += pow(sum, 2);
        }
        return fitness;
    }
}
