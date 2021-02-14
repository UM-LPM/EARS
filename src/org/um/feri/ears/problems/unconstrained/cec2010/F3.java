package org.um.feri.ears.problems.unconstrained.cec2010;

import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.Collections;

public class F3 extends CEC2010 {

    int[] P;

    public F3(int d) {
        super(d, 3);

        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -32.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 32.0));

        name = "F03 Shifted Ackley's Function";

        OShift = new double[numberOfDimensions];
        for (int i = 0; i < numberOfDimensions; i++) {
            OShift[i] = Util.nextDouble(lowerLimit.get(i), upperLimit.get(i));
        }

    }

    @Override
    public double eval(double[] x) {
        return Functions.ackley_func(x, numberOfDimensions, OShift, M, 1, 0);
    }

}
