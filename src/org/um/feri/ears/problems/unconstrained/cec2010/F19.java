package org.um.feri.ears.problems.unconstrained.cec2010;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.random.RNG;

public class F19 extends CEC2010 {

    public F19(int d) {
        super("F19 Shifted Schwefel's Problem 1.2", d, 19);

        oShift = new double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            oShift[i] = RNG.nextDouble(lowerLimit.get(i), upperLimit.get(i));
        }
        decisionSpaceOptima[0] = oShift;

        M = new double[m * m];

        DenseMatrix64F A = RandomMatrices.createOrthogonal(m, m, RNG.getSelectedRng());

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                M[i * m + j] = A.get(i, j);
            }
        }
    }

    @Override
    public double eval(double[] x) {
        return Functions.schwefel_func(x, numberOfDimensions, oShift, M, 1, 0);
    }
}
