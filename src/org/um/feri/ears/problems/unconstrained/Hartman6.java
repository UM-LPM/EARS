package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Hartman6 extends Problem {
	
	public double[][] a;
	public double[][] p;
	public double[] c;
	

	public Hartman6() {
		super(6,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.0));
		name = "Hartman6";
		
		optimum[0][0] = 0.201690;
		optimum[0][1] = 0.150011;
		optimum[0][2] = 0.476874;
		optimum[0][3] = 0.275332;
		optimum[0][4] = 0.311652;
		optimum[0][5] = 0.657301;
		
		a = new double[][] {
				{10,3,17,3.5,1.7,8},
				{0.05,10,17,0.1,8,14},
				{3,3.5,1.7,10,17,8},
				{17,8,0.05,10,0.1,14}
		};
		p = new double[][] {
				{0.1312, 0.1696, 0.5569, 0.0124, 0.8283, 0.5886},	
				{0.2329, 0.4135, 0.8307, 0.3736, 0.1004, 0.9991},	
				{0.2348, 0.1415, 0.3522, 0.2883, 0.3047, 0.6650},	
				{0.4047, 0.8828, 0.8732, 0.5743, 0.1091, 0.0381}
		};
		c = new double[] {1, 1.2, 3, 3.2};

	}
	
	public double eval(double x[]) {
		double v = 0;
		double v_1 = 0;
		for (int i = 0; i < 4; i++){
			v_1 = 0;
			for (int j = 0; j < numberOfDimensions; j++){
			   v_1 = v_1 + a[i][j]*Math.pow(x[j]-p[i][j],2);
			}
			v = v + c[i]*Math.exp(v_1*(-1));
		}
		v = v * (-1);
		return v;
	}

	public double getOptimumEval() {
		return -3.32187706;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
