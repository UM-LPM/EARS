package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Dixon_Price extends Problem {
	public Dixon_Price(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
		name = "Dixon_Price";
		
		for (int i = 0; i < numberOfDimensions; i++) {
			double minX = Math.pow(2, -(Math.pow(2, i+1) - 2) / Math.pow(2, i+1));
			optimum[0][i] = minX;
		}
	}
	
	public double eval(double x[]) {
		double v = 0;
		for (int i = 1; i < numberOfDimensions; i++) {
			v = v + (i+1)*Math.pow(2*x[i]*x[i]-x[i-1], 2);
		}
		v = Math.pow(x[0] - 1, 2) + v;
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
