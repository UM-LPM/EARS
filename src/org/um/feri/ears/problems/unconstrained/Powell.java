package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Powell extends Problem {
	
	public Powell(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -4.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
		name = "Powell";
	}
	
	public double eval(double x[]) {
		double v = 0;
		int k = 4;
		for (int i = 0; i < numberOfDimensions/k; i++) {
			v = v
				+ Math.pow(x[4*(i+1)-3-1] + 10*x[4*(i+1)-2-1],2)
				+ 5*Math.pow(x[4*(i+1)-1-1] - x[4*(i+1)-1],2)
				+ Math.pow(x[4*(i+1)-2-1] - x[4*(i+1)-1-1],4)
				+ Math.pow(x[4*(i+1)-3-1] - x[4*(i+1)-1],4);
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
