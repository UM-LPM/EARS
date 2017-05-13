package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Zakharov extends Problem {
	
	public Zakharov(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
		name = "Zakharov";
	}
	
	public double eval(double x[]) {
		double v = 0;
		double v_1 = 0, v_2 = 0, v_3 = 0;
		for (int i = 0; i < numberOfDimensions; i++) {
			v_1 = v_1 + Math.pow(x[i],2);
		}
		for (int i = 0; i < numberOfDimensions; i++) {
			v_2 = v_2 + 0.5*(i+1)*x[i];
		}
		v_2 = Math.pow(v_2,2);
		for (int i = 0; i < numberOfDimensions; i++) {
			v_3 = v_3 + 0.5*(i+1)*x[i];
		}
		v_3 = Math.pow(v_3,4);
		v = v_1 + v_2 + v_3;
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
