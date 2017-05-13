package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

/**
 * https://www.sfu.ca/~ssurjano/beale.html
 *
 */

public class Beale extends Problem {
	
	public Beale() {
		super(2,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -4.5));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 4.5));
		name = "Beale";
		
		optimum[0][0] = 3;
		optimum[0][1] = 0.5;
	}
	
	public double eval(double x[]) {
		double v = 0;
		v =   Math.pow(1.500 - x[0] + x[0]*x[1], 2)
			+ Math.pow(2.250 - x[0] + x[0]*x[1]*x[1], 2)
			+ Math.pow(2.625 - x[0] + x[0]*x[1]*x[1]*x[1], 2);
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
