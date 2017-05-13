package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Colville extends Problem {
	
	public Colville() {
		super(4,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
		name = "Colville";
		
		Arrays.fill(optimum[0], 1);
	}
	
	public double eval(double x[]) {
		double v = 0;
		v = 100*(Math.pow(x[0]*x[0] - x[1],2))
			+ Math.pow(x[0]-1,2)
			+ Math.pow(x[2]-1,2)
			+ 90*Math.pow(x[2]*x[2]-x[3],2)
			+ 10.1*(Math.pow(x[1]-1,2)+Math.pow(x[3]-1,2))
			+ 19.8*(x[1]-1)*(x[3]-1);
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
