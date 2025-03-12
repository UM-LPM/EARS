package org.um.feri.ears.problems.unconstrained.cec2010;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.random.RNG;

public class F15 extends CEC2010 {

    public F15(int d) {
        super("F15 D/m-group Shifted and m-rotated Rastrigin's Function", d, 15);

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
        double F = 0;
        int max = (numberOfDimensions / (m << 1));
        int from, to;

        double[] p1;
        double[] s1;

        for (int k = 0; k < max; k++) {
            from = k * m;
            to = (k + 1) * m - 1;
            p1 = getPermutatedIndices(x, P, from, to - from);
            s1 = getPermutatedIndices(oShift, P, from, to - from);
            F += Functions.rastrigin_func(p1, to - from, s1, M, 1, 1);
        }
        return F;
    }
}
