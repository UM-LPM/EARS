package org.um.feri.ears.problems.unconstrained.cec2010;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.random.RNG;

public class F20 extends CEC2010 {

    public F20(int d) {
        super("F20 Shifted Rosenbrock's Function", d, 20);

        OShift = new double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            OShift[i] = RNG.nextDouble(lowerLimit.get(i), upperLimit.get(i));
        }

        M = new double[m * m];

        DenseMatrix64F A = RandomMatrices.createOrthogonal(m, m, RNG.getAsRandom());

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                M[i * m + j] = A.get(i, j);
            }
        }
    }

    @Override
    public double eval(double[] x) {
        return Functions.rosenbrock_func(x, numberOfDimensions, OShift, M, 1, 0);
    }
}
