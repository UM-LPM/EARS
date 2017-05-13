package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.util.Util;

/**
 * http://al-roomi.org/benchmarks/unconstrained/n-dimensions/161-quartic-or-modified-4th-de-jong-s-function
 *
 */
public class Quartic extends Problem {
	
	boolean noise = false;
	
	public Quartic(int d) {
		this(d,false);
	}
	
	
	public Quartic(int d, boolean noise) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -1.28));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.28));
		this.noise = noise;
		name = "Quartic / Modified De Jong" + (noise? " with noise":"");
	}
	
	public double eval(double x[]) {
		double v = 0;
		for (int i = 0; i < numberOfDimensions; i++) {
			v = v + (i+1)*Math.pow(x[i],4);
		}
		if(noise){
			v = v + Util.nextDouble();
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
