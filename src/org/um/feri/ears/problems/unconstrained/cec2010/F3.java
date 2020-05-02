package org.um.feri.ears.problems.unconstrained.cec2010;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;
import org.um.feri.ears.util.Util;

public class F3 extends CEC2010{

	int[] P;
	
	public F3(int d) {
		super(d, 3);
		
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -32.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 32.0));
		
		name = "F03 Shifted Ackley's Function";
		
		OShift = new double[numberOfDimensions];
		for (int i=0; i<numberOfDimensions; i++){
			OShift[i] = Util.nextDouble(lowerLimit.get(i),upperLimit.get(i));
		}
		
	}

	@Override
	public double eval(double[] x) {
		double F = 0;
		F = Functions.ackley_func(x, numberOfDimensions, OShift, M, 1, 0);
		return F;
	}

}
