package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

/**
 * https://www.sfu.ca/~ssurjano/booth.html
 *
 */

public class Booth extends Problem {
	
	public double[][] a;
	
	public Booth() {
		super(2,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
		name = "Booth";
		
		optimum[0][0] = 1;
		optimum[0][1] = 3;
	}
	
	public double eval(double x[]) {
		double v = 0;
		v = Math.pow(x[0] + 2*x[1] - 7, 2)
		  + Math.pow(2*x[0] + x[1] - 5, 2);
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
