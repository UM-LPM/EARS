package org.um.feri.ears.problems.unconstrained.cec2010;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.Util;

public class F2 extends CEC2010{
	
	int[] P;

	public F2(int d) {
		super(d, 2);
		
		
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
		
		name = "F02 Shifted Rastrigin's Function";
		OShift = new double[numberOfDimensions];

		for (int i=0; i<numberOfDimensions; i++){
			OShift[i] = Util.nextDouble(lowerLimit.get(i),upperLimit.get(i));
		}

	}

	@Override
	public double eval(double[] x) {
		double F = 0;
		F = Functions.rastrigin_func(x, numberOfDimensions, OShift, M, 1, 0);
		return F;
	}

}
