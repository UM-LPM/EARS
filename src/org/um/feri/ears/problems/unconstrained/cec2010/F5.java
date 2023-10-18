package org.um.feri.ears.problems.unconstrained.cec2010;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.random.RNG;

public class F5 extends CEC2010 {

    public F5(int d) {
        super("F05 Single-group Shifted and m-rotated Rastrigin's Function", d, 5);

        P = new int[numberOfDimensions];
        P = RNG.randomPermutation(numberOfDimensions);
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
        double[] p1 = getPermutatedIndices(x, P, 0, m);
        double[] p2 = getPermutatedIndices(x, P, m, numberOfDimensions - m);
        double[] s1 = getPermutatedIndices(OShift, P, 0, m);
        double[] s2 = getPermutatedIndices(OShift, P, m, numberOfDimensions - m);

        return Functions.rastrigin_func(p1, m, s1, M, 1, 1) * 1000000 + Functions.rastrigin_func(p2, numberOfDimensions - m, s2, M, 1, 0);
    }

}
