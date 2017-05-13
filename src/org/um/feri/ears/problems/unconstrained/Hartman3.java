package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Hartman3 extends Problem {
	
	public double[][] a;
	public double[][] p;
	public double[] c;

	public Hartman3() {
		super(3,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 1.0));
		name = "Hartman3";
		
		optimum[0][0] = 0.114614;
		optimum[0][1] = 0.555649;
		optimum[0][2] = 0.852547;

		a = new double[][] {
				{3,10,30},
				{0.1,10,35},
				{3,10,30},
				{0.1,10,35}
		};
		p = new double[][] {
				{0.3689,0.1170,0.2673},	
				{0.4699, 0.4387, 0.7470},	
				{0.1091, 0.8732, 0.5547},	
				{0.03815, 0.5743, 0.8828}
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
		return -3.86278215;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
