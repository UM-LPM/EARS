package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Schwefel1_2 extends Problem {
	
	public Schwefel1_2(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
		name = "Schwefel 1.2";
	}
	
	public double eval(double x[]) {
		double v = 0;
		double v_1 = 0;
		for (int i = 0; i < numberOfDimensions; i++) {
			v_1 = 0;
			for (int j = 0; j < i; j++) {
				v_1 = v_1 + x[j];
			}
			v = v + Math.pow(v_1, 2);
		}
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
