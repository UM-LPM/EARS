package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;
/**
 * http://al-roomi.org/benchmarks/unconstrained/n-dimensions/176-generalized-schwefel-s-problem-2-26
 *
 */
public class Schwefel2_26 extends Problem {
	
	public Schwefel2_26(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -500.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 500.0));
		name = "Schwefel 2.26";
		
		Arrays.fill(optimum[0], 420.968746);
	}
	
	public double eval(double x[]) {
		double v = 0;
		for (int i = 0; i < numberOfDimensions; i++){
			v += -x[i]*Math.sin(Math.sqrt(Math.abs(x[i])));
		}
		return v;
	}

	public double getOptimumEval() {
		//return -12569.5;
		return -418.982887272 * numberOfDimensions;
	}

	@Override
	public double[][] getOptimalVector() {
		return super.getOptimalVector();
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
