package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Shekel5 extends Problem {
	
	public double[][] a;
	public double[] c;
	
	public Shekel5() {
		super(4,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 10.0));
		name = "Shekel5";
		
		Arrays.fill(optimum[0], 4);
		
		a = new double[][] {
				{4,4,4,4},	
				{1,1,1,1},	
				{8,8,8,8},	
				{6,6,6,6},	
				{3,7,3,7},	
				{2,9,2,9},	
				{5,5,3,3},	
				{8,1,8,1},	
				{6,2,6,2},	
				{7,3.6,7,3.6},	
		};
		c = new double[] {0.1,0.2,0.2,0.4,0.4,0.6,0.3,0.7,0.5,0.5};

	}
	
	public double eval(double x[]) {
		double v = 0;
		double v_1 = 0;
		for (int i = 0; i < 5; i++){
			v_1 = 0;
			for (int j=0;j<numberOfDimensions;j++){
				v_1 = v_1 + Math.pow(x[j]-a[i][j],2);
			}
			v = v + Math.pow(c[i] + v_1,-1);
		}
		v = v * (-1);
		return v;
	}

	public double getOptimumEval() {
		return -10.15319585;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
