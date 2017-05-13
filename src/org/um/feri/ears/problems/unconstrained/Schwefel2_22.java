package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Schwefel2_22 extends Problem {
	public Schwefel2_22(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -10.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
		name = "Schwefel 2.22";
	}
	
	public double eval(double x[]) {
		double v = 0;
		double v_1 = 0, m_1 = 1;
		for (int i = 0; i < numberOfDimensions; i++) {
			v_1 = v_1 + Math.abs(x[i]);
			m_1 = m_1 * Math.abs(x[i]);
		}
		v = v_1 + m_1;
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
