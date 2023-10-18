package org.um.feri.ears.problems.unconstrained.cec2010;

import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Collections;

public class F1 extends CEC2010 {

    int[] P;

    public F1(int d) {
        super("F01 Shifted Elliptic Function", d, 1);

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));

        OShift = new double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            OShift[i] = RNG.nextDouble(lowerLimit.get(i), upperLimit.get(i));
        }
    }

    public double eval(double[] x) {
        return Functions.ellips_func(x, numberOfDimensions, OShift, M, 1, 0);
    }

}
