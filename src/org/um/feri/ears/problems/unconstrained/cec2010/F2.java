package org.um.feri.ears.problems.unconstrained.cec2010;

import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Collections;

public class F2 extends CEC2010 {

    int[] P;

    public F2(int d) {
        super("F02 Shifted Rastrigin's Function", d, 2);

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -5.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 5.0));

        OShift = new double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            OShift[i] = RNG.nextDouble(lowerLimit.get(i), upperLimit.get(i));
        }
    }

    @Override
    public double eval(double[] x) {
        return Functions.rastrigin_func(x, numberOfDimensions, OShift, M, 1, 0);
    }

}
