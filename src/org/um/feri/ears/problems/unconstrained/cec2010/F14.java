package org.um.feri.ears.problems.unconstrained.cec2010;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.random.RNG;

public class F14 extends CEC2010 {

    public F14(int d) {
        super("F14 D/m-group Shifted and m-rotated Elliptic Function", d, 14);

        P = new int[numberOfDimensions];
        P = RNG.randomPermutation(numberOfDimensions);
        OShift = new double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            OShift[i] = RNG.nextDouble(lowerLimit.get(i), upperLimit.get(i));
        }

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
        double F = 0;
        int max = (numberOfDimensions / (m << 1));
        int from, to;

        double[] p1;
        double[] s1;

        for (int k = 0; k < max; k++) {
            from = k * m;
            to = (k + 1) * m - 1;
            p1 = getPermutatedIndices(x, P, from, to - from);
            s1 = getPermutatedIndices(OShift, P, from, to - from);
            F += Functions.ellips_func(p1, to - from, s1, M, 1, 1);
        }

        return F;
    }
}
