package org.um.feri.ears.problems.unconstrained;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

import java.util.ArrayList;
import java.util.Collections;

public class Langermann extends Problem {

    static final double[][] a = new double[][]{{3.0, 5.0, 2.0, 1.0, 7.0}, {5.0, 2.0, 1.0, 4.0, 9.0}};
    static final double[] c = new double[]{1.0, 2.0, 5.0, 2.0, 3.0};
    static final double m = 5;

    public Langermann() {
        super(2,0);
        lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
        upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
        name = "Langermann";
    }

    @Override
    public double eval(double[] x) {
        double sum1 = 0.0;
        for (int i = 1; i <= m; i++) {
            double sum2 = 0.0;
            for (int j = 1; j <= numberOfDimensions; j++) {
                double xj = x[(j - 1)];
                sum2 += (xj - a[(j - 1)][(i - 1)]) * (xj - a[(j - 1)][(i - 1)]);
            }
            sum1 += c[(i - 1)] * Math.exp(-1.0/Math.PI * sum2) * Math.cos(Math.PI * sum2);
        }
        return sum1;
    }

    @Override
    public double eval(Double[] ds) {
        return eval(ArrayUtils.toPrimitive(ds));
    }
}
