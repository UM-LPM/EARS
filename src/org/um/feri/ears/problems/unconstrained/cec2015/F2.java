package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F2 extends CEC2015 {

    public F2(int d) {
        super(d, 2);

        name = "F02 Discus Function";
    }

    @Override
    public double eval(double[] x) {
        double F;
        F = Functions.discus_func(x, numberOfDimensions, OShift, M, 1, 1);
        F += 100 * func_num;
        return F;
    }

}
