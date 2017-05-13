package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Stepint extends Problem {
	public Stepint(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.12));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.12));
		name = "Stepint";
		
		//infinite number of global optima
		// -5.12 <= x < -5.0
		Arrays.fill(optimum[0], -5.12);
	}
	
	public double eval(double x[]) {
		double v = 0;
		for (int i = 0; i < numberOfDimensions; i++) {
			v = v + Math.floor(x[i]);
		}
		v = v + 25.0;
		return v;
	}

	public double getOptimumEval() {
		return 25 - 6 * numberOfDimensions;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
