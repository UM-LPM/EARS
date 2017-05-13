package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Step extends Problem {
	public Step(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
		name = "Step";
	}
	
	public double eval(double x[]) {
		double v = 0;
		for (int i = 0; i < numberOfDimensions; i++) {
			v = v + Math.pow(Math.floor(x[i] + 0.5),2);
		}
		return v;
	}

	public double getOptimumEval() {
		return 0;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
