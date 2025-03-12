package org.um.feri.ears.problems.unconstrained.cec2010;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.random.RNG;

public class F7 extends CEC2010 {

    public F7(int d) {
        super("F07 Single-group Shifted m-dimensional Schwefel's Problem 1.2", d, 7);

        P = new int[numberOfDimensions];
        P = RNG.randomPermutation(numberOfDimensions);
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
        double[] p1 = getPermutatedIndices(x, P, 0, m);
        double[] p2 = getPermutatedIndices(x, P, m, numberOfDimensions - m);
        double[] s1 = getPermutatedIndices(oShift, P, 0, m);
        double[] s2 = getPermutatedIndices(oShift, P, m, numberOfDimensions - m);

        return Functions.schwefel_func(p1, m, s1, M, 1, 0) * 1000000 + Functions.sphere_func(p2, numberOfDimensions - m, s2, M, 1, 0);
    }

}
