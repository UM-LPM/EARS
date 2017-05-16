package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

/**
 * http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page295.htm
 * https://www.sfu.ca/~ssurjano/ackley.html
 *
 */

public class Ackley extends Problem {

	public Ackley(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -32.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 32.0));
		name = "Ackley";
	}
	
	@Override
	public double eval(double x[]) {
		double v = 0;
		double sphere_sum=0;
		double cos_sum=0;
		for (int i=0; i<numberOfDimensions; i++){
			sphere_sum += x[i]*x[i];
			cos_sum    += Math.cos(2*Math.PI*x[i]);
		}
		v = -20*Math.exp(-0.2*Math.sqrt(1.0/numberOfDimensions * sphere_sum)) - Math.exp(1.0/numberOfDimensions * cos_sum) + 20.0 + Math.E;
		return v;
	}

	public double getOptimumEval() {
		return 0;
	}

	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
}
