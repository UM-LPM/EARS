package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class PowerSum extends Problem {
	
	public double[] b;
	
	public PowerSum() {
		super(4,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, numberOfDimensions*1.0));
		name = "PowerSum";
		b = new double[] {8,18,44,114};

	}
	
	public double eval(double x[]) {
		double v = 0;
		double v_1 = 0;
		for (int i = 0; i < numberOfDimensions; i++){
			v_1 = 0;
			for (int j = 0; j < numberOfDimensions; j++){
			   v_1 = v_1 + Math.pow(x[j],i+1);
			}
			v = v + Math.pow(v_1 - b[i], 2);
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
