package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

/**
 * https://www.sfu.ca/~ssurjano/branin.html
 *
 */

public class Branin extends Problem {
	
	public double[][] a;
	
	public Branin() {
		super(2,0,3);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		name = "Branin";
		
		lowerLimit.set(0, -5.0);
		upperLimit.set(0, 10.0);
		
		lowerLimit.set(1, 0.0);
		upperLimit.set(1, 15.0);
		
		optimum[0][0] = -Math.PI;
		optimum[0][1] = 12.275;
		
		optimum[1][0] = Math.PI;
		optimum[1][1] = 2.275;
		
		optimum[2][0] = 3*Math.PI;
		optimum[2][1] = 2.425;
	}
	
	public double eval(double x[]) {
		double v = 0;
		v = Math.pow(x[1] - (5.1/(4*Math.PI*Math.PI))*x[0]*x[0] + (5.0/Math.PI)*x[0] - 6, 2)+10*(1 - 1.0/(8.0*Math.PI))*Math.cos(x[0])+10;
		return v;
	}

	public double getOptimumEval() {
		return 0.39788735;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
