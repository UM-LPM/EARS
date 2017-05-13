package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Shubert extends Problem {
	public Shubert(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
		name = "Shubert";
		//miltiple global optima
	}
	
	public double eval(double x[]) {
		double v = 0;
		double m1 = 0, m2 = 0;
		for (int i = 0; i < 5; i++) {
			m1 = m1 + (i+1)*Math.cos(((i+1)+1)*x[0]+(i+1));
			m2 = m2 + (i+1)*Math.cos(((i+1)+1)*x[1]+(i+1));
		}
		v = m1*m2;
		return v;
	}

	public double getOptimumEval() {
		return -186.7309;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
