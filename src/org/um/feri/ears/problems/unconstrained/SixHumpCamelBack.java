package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class SixHumpCamelBack extends Problem {
	public SixHumpCamelBack() {
		super(2,0,2);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
		// -3 <= x1 <= 3
		// -2 <= x1 <= 2
		name = "SixHumpCamelBack";
		
		optimum[0][0] = -0.0898;
		optimum[0][1] = 0.7126;
		
		optimum[1][0] = 0.0898;
		optimum[1][1] = -0.7126;
	}
	
	public double eval(double x[]) {
		double v = 0;
		v = 4*Math.pow(x[0],2)
			- 2.1*Math.pow(x[0],4)
			+ (1.0/3.0)*Math.pow(x[0],6)
			+ x[0]*x[1]
			- 4*Math.pow(x[1],2)
			+ 4*Math.pow(x[1],4);
		return v;
	}

	public double getOptimumEval() {
		return -1.03162842;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
