package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Michalewicz2 extends Problem {
	
	public Michalewicz2() {
		super(2,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, Math.PI));
		name = "Michalewicz2";
		
		optimum[0][0] = 2.20290552;
		optimum[0][1] = 1.57079633;
	}
	
	public double eval(double x[]) {
		double v = 0;
		int m = 10;
		for (int i = 0; i < numberOfDimensions; i++){
			v = v + Math.sin(x[i])*Math.pow(Math.sin((i+1)*x[i]*x[i]/Math.PI), 2*m);
		}
		v = v * (-1);
		return v;
	}

	public double getOptimumEval() {
		return -1.80130341;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
