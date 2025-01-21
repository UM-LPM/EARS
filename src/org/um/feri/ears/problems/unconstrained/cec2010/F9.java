package org.um.feri.ears.problems.unconstrained.cec2010;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.random.RNG;

public class F9 extends CEC2010{
	
	public F9(int d) {
		super("F09 D/2m-group Shifted and m-rotated Elliptic Function", d, 9);
		
		P = new int[numberOfDimensions];
		P = RNG.randomPermutation(numberOfDimensions);
		oShift = new double[numberOfDimensions];

		for (int i=0; i<numberOfDimensions; i++){
			oShift[i] = RNG.nextDouble(lowerLimit.get(i),upperLimit.get(i));
		}
		decisionSpaceOptima[0] = oShift;

		M = new double[m*m];
		
		DenseMatrix64F A = RandomMatrices.createOrthogonal(m, m, RNG.getSelectedRng());
		
		for (int i=0; i<m; i++){
			for (int j=0; j<m; j++){
				M[i * m + j] = A.get(i, j);
			}
		}
	}

	@Override
	public double eval(double[] x) {
		double F = 0;
		int max = (numberOfDimensions / (m << 1));
		int from, to;
		int from2 = numberOfDimensions / 2;
		
		double[] p1;
		double[] s1;
		
		double[] p2 = getPermutatedIndices(x,P,from2,numberOfDimensions - from2);
		double[] s2 = getPermutatedIndices(oShift,P,from2,numberOfDimensions - from2);

		for (int k = 0; k < max; k++) {
			from = k*m;
			to = (k+1)*m - 1;
			p1 = getPermutatedIndices(x,P,from,to-from);
			s1 = getPermutatedIndices(oShift,P,from,to-from);
			F += Functions.ellips_func(p1, to-from, s1, M, 1, 1);
		}

		F += Functions.sphere_func(p2, numberOfDimensions - from2, s2, M, 1, 0);
		
		return F;
	}
}
