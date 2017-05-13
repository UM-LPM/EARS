package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Bohachevsky1 extends Problem {
	

	public Bohachevsky1() {
		super(2,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
		name = "Bohachevsky1";
	}
	
	public double eval(double x[]) {
		double v = 0;
		v = Math.pow(x[0], 2)
		  + 2*Math.pow(x[1], 2)
		  - 0.3*Math.cos(3*Math.PI*x[0])
		  - 0.4*Math.cos(4*Math.PI*x[1])
		  + 0.7;
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
