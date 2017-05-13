package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Matyas extends Problem {
	
	public Matyas() {
		super(2,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
		name = "Matyas";
	}
	
	public double eval(double x[]) {
		double v = 0;
		v = 0.26*(Math.pow(x[0],2) + Math.pow(x[1],2)) - 0.48*x[0]*x[1];
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
