package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;


public class F6 extends CEC2015 {

    public F6(int d) {
        super(d, 6);

        name = "F06 HappyCat Function";
    }

    @Override
    public double eval(double[] x) {
        double F;
        F = Functions.happycat_func(x, numberOfDimensions, OShift, M, 1, 1);
        F += 100 * func_num;
        return F;
    }

}
