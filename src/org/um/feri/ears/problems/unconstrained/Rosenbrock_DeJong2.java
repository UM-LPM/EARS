package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

/**
 * https://www.sfu.ca/~ssurjano/rosen.html
 * http://www.geatbx.com/ver_3_5/fcnfun2.html
 */
public class Rosenbrock_DeJong2 extends Problem {
	public Rosenbrock_DeJong2(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -2.048));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 2.048));
		name = "Rosenbrock - De Jong's function 2";
		
		Arrays.fill(optimum[0], 1);
	}
	
	public double eval(double x[]) {
		double v = 0;
		for (int i = 0; i < numberOfDimensions-1; i++) {
			v = v + (100*Math.pow(x[i+1]-x[i]*x[i], 2) + Math.pow(x[i]-1,2));
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
