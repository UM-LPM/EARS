package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Perm extends Problem {
	
	double beta = 0.5;
	
	public Perm(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -numberOfDimensions*1.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, numberOfDimensions*1.0));
		name = "Perm";
		
		for (int i = 0; i < numberOfDimensions; i++) {
			optimum[0][i] = i+1;
		}
	}
	
	public double eval(double x[]) {
		double v = 0;
		double v_1 = 0;
		for (int i = 0; i < numberOfDimensions; i++){
			v_1 = 0;
			for (int j = 0; j < numberOfDimensions; j++){
				v_1 = v_1 + (Math.pow(j+1, i) + beta)*(Math.pow(x[j]/(j+1), i)-1);
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
