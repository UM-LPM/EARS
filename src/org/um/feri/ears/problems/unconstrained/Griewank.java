package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

/**
 * https://www.sfu.ca/~ssurjano/griewank.html
 *
 */

public class Griewank extends Problem {

	public Griewank(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -600.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 600.0));
		name = "Griewank";
	}
	
	public double eval(double x[]) {
		double v = 0;
		double v_1 = 0;
		double m_1 = 1;
		for (int i = 0; i < numberOfDimensions; i++){
			v_1 = v_1 + x[i]*x[i];
			m_1 = m_1 * Math.cos(x[i]/Math.sqrt(i+1));
		}
		v = (1.0/4000.0)*v_1 - m_1 + 1;
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
