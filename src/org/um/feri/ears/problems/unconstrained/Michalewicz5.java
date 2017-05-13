package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Michalewicz5 extends Problem {
	
	public Michalewicz5() {
		super(5,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, Math.PI));
		name = "Michalewicz5";
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
		return -4.68765817;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
