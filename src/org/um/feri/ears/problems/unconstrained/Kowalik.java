package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Kowalik extends Problem {
	
	public double[] a;
	public double[] b;
	
	public Kowalik() {
		super(4,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -5.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 5.0));
		name = "Kowalik";
		
		optimum[0][0] = 0.192833;
		optimum[0][1] = 0.190836;
		optimum[0][2] = 0.123117;
		optimum[0][3] = 0.135766;
		
		a = new double[] {0.1957,0.1947,0.1735,0.1600,0.0844,0.0627,0.0456,0.0342,0.0323,0.0235,0.0246};
		b = new double[] {0.25,0.5,1,2,4,6,8,10,12,14,16};
		
	}
	
	public double eval(double x[]) {
		double v = 0;
		
		for (int i = 0; i < 11; i++){
			v = v + Math.pow(a[i] - (x[0]*((1.0/b[i])*(1.0/b[i])+(1.0/b[i])*x[1]))/((1.0/b[i])*(1.0/b[i])+(1.0/b[i])*x[2]+x[3]),2);
		}
		
		return v;
	}

	public double getOptimumEval() {
		return 0.00030749;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
