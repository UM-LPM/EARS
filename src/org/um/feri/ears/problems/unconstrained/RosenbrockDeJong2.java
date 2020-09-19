package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import static java.lang.Math.*;

/**
 * https://www.sfu.ca/~ssurjano/rosen.html
 * http://www.geatbx.com/ver_3_5/fcnfun2.html
 */
public class RosenbrockDeJong2 extends Problem {
    public RosenbrockDeJong2(int d) {
        super(d, 0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        //lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2.048));
        //upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.048));
        name = "Rosenbrock - De Jong's function 2";

        Arrays.fill(optimum[0], 1);
    }

    @Override
    public double eval(double[] x) {
        double fitness = 0;
        for (int i = 0; i < numberOfDimensions - 1; i++) {
            fitness = fitness + (100 * pow(x[i + 1] - x[i] * x[i], 2) + pow(x[i] - 1, 2));
        }
        return fitness;
    }
}
