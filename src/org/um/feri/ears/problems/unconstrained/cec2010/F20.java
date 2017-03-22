package org.um.feri.ears.problems.unconstrained.cec2010;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.RandomMatrices;
import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.Util;

public class F20 extends CEC2010{
	
	public F20(int d) {
		super(d, 20);
		
		name = "F20 Shifted Rosenbrock's Function";
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
		F = Functions.rosenbrock_func(x, numberOfDimensions, OShift, M, 1, 0);
		return F;
	}

}
