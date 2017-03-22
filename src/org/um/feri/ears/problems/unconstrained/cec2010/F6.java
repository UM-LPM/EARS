package org.um.feri.ears.problems.unconstrained.cec2010;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.Util;

public class F6 extends CEC2010{
	
	public F6(int d) {
		super(d, 6);
		
		name = "F06 Single-group Shifted and m-rotated Ackley's Function";
		
		P = new int[numberOfDimensions];
		P = Util.randomPermutation(numberOfDimensions);
		OShift = new double[numberOfDimensions];

		for (int i=0; i<numberOfDimensions; i++){
			OShift[i] = Util.nextDouble(lowerLimit.get(i),upperLimit.get(i));
		}
		
		M = new double[m*m];
		
		DenseMatrix64F A = RandomMatrices.createOrthogonal(m, m, Util.rnd);
		
		for (int i=0; i<m; i++){
			for (int j=0; j<m; j++){
				M[i * m + j] = A.get(i, j);
			}
		}
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F = 0;
		double[] p1 = getPermutatedIndices(x,P,0,m);
		double[] p2 = getPermutatedIndices(x,P,m,numberOfDimensions - m);
		double[] s1 = getPermutatedIndices(OShift,P,0,m);
		double[] s2 = getPermutatedIndices(OShift,P,m,numberOfDimensions - m);
		
		F = Functions.ackley_func(p1, m, s1, M, 1, 1) * 1000000 + Functions.ackley_func(p2, numberOfDimensions - m, s2, M, 1, 0);
		return F;
	}

}
