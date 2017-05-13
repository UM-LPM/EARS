package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Trid6 extends Problem {
	
	public Trid6() {
		super(6,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -Math.pow(numberOfDimensions, numberOfDimensions)));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, Math.pow(numberOfDimensions, numberOfDimensions)));
		name = "Trid6";
		
		for (int i = 0; i < numberOfDimensions; i++) {
			optimum[0][i] = (i+1) * (numberOfDimensions + 1 - (i+1));
		}
		
	}
	
	public double eval(double x[]) {
		double v = 0;
		double v_1 = 0, v_2 = 0;
		for (int i = 0; i < numberOfDimensions; i++) {
			v_1 = v_1 + Math.pow((x[i]-1),2);
		}
		for (int i = 1; i < numberOfDimensions; i++) {
			v_2 = v_2 + x[i]*x[i-1];
		}
		v = v_1 - v_2;
		return v;
	}

	public double getOptimumEval() {
		
		double opt = - (numberOfDimensions*(numberOfDimensions+4) * (numberOfDimensions-1)) / 6;
		
		return opt;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
