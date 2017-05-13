package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

/**
 * https://www.sfu.ca/~ssurjano/easom.html
 *
 */
public class Easom extends Problem {
	
	public Easom() {
		super(2,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
		name = "Easom";
		
		Arrays.fill(optimum[0], Math.PI);
	}
	
	public double eval(double x[]) {
		double v = 0;
		v = -1*Math.cos(x[0])*Math.cos(x[1])*Math.exp(-1*Math.pow(x[0]-Math.PI, 2)-Math.pow(x[1]-Math.PI, 2));
		return v;
	}

	public double getOptimumEval() {
		return -1;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
