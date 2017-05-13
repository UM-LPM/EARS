package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

/**
 * https://www.sfu.ca/~ssurjano/rastr.html
 *
 */
public class Rastrigin extends Problem {
	
	public double[][] a;
	
	public Rastrigin(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.12));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.12));
		name = "Rastrigin";
	}
	
	public double eval(double x[]) {
		double v = 0;
		for (int i = 0; i < numberOfDimensions; i++){
			v += x[i]*x[i] - 10*Math.cos(2*Math.PI*x[i]);
		}
		v += 10 * numberOfDimensions;
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
