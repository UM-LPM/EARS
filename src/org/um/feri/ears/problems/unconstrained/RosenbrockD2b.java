package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

/*
    http://www.geatbx.com/docu/fcnindex-01.html#P86_3059
 */
public class RosenbrockD2b extends Problem {

    public RosenbrockD2b() {
        super(2, 0);

        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));

        name = "Rosenbrock d2b";

        Arrays.fill(optimum[0], 1);
    }

    @Override
    public double eval(double[] x) {
        double result = 0;
        for (int i = 0; i < (numberOfDimensions - 1); i++) {
            result += 100 * (x[i + 1] - x[i] * x[i]) * (x[i + 1] - x[i] * x[i]) + (1 - x[i]) * (1 - x[i]);
            //v+=Math.pow(1-ds[i],2)+100*Math.pow(ds[i+1]-ds[i]*ds[i],2);
        }
        return result;
    }
}
