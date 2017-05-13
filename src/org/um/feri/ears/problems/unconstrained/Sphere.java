package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;
/**
 * https://www.sfu.ca/~ssurjano/spheref.html
 *
 */
public class Sphere extends Problem {
	public Sphere(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -100.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 100.0));
		name = "Sphere";
	}
	
	public double eval(double x[]) {
		double v = 0;
		for (int i = 0; i < numberOfDimensions; i++) {
			v = v + Math.pow(x[i],2);
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
