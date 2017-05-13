package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Schaffer extends Problem {
	public Schaffer(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
		name = "Schaffer";
	}
	
	public double eval(double x[]) {
		double v = 0;
		v = 0.5 + (Math.pow(Math.sin(Math.sqrt(x[0]*x[0] + x[1]*x[1])),2) - 0.5)/(Math.pow(1+0.001*(x[0]*x[0] + x[1]*x[1]),2));
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
