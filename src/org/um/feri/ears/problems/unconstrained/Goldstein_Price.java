package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Goldstein_Price extends Problem {
	public Goldstein_Price() {
		super(2,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.0));
		name = "Goldstein Price";
		
		optimum[0][0] = 0;
		optimum[0][1] = -1;
	}
	
	public double eval(double x[]) {
		double v = 0;
		
		v = (1+Math.pow(x[0]+x[1]+1,2)*(19-14*x[0]+3*x[0]*x[0]-14*x[1]+6*x[0]*x[1]+3*x[1]*x[1]))*
			(30+Math.pow(2*x[0]-3*x[1],2)*(18-32*x[0]+12*x[0]*x[0]+48*x[1]-36*x[0]*x[1]+27*x[1]*x[1]));
		
		return v;
	}

	public double getOptimumEval() {
		return 3;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
