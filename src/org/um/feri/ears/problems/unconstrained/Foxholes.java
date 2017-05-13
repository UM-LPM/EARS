package org.um.feri.ears.problems.unconstrained;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

public class Foxholes extends Problem {
	
	public double[][] a;
	
	public Foxholes(int d) {
		super(d,0);
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, -65.536));
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 65.536));
		name = "Shekel's Foxholes";
		
		Arrays.fill(optimum[0], -31.97833);
		
		a = new double[][] {{-32,-32},
				            {-16,-32},
				            {0,-32},
				            {16,-32},
				            {32,-32},
				            {-32,-16},
				            {-16,-16},
				            {0,-16},
				            {16,-16},
				            {32,-16},
				            {-32,0},
				            {-16,0},
				            {0,0},
				            {16,0},
				            {32,0},
				            {-32,16},
				            {-16,16},
				            {0,16},
				            {16,16},
				            {32,16},
				            {-32,32},
				            {-16,32},
				            {0,32},
				            {16,32},
				            {32,32}
				           };
	}
	
	public double eval(double x[]) {
		double v = 0;
		double v_1 = 0;
		for (int j = 0; j < 25; j++) {
			v_1 = 0;
			for (int i = 0; i < numberOfDimensions; i++) {
				v_1 = v_1 + Math.pow(x[i] - a[j][i],6);
			}
			v_1 += j+1;
			v = v + 1.0/v_1;
		}
		v = v + 1.0/500.0;
		v = Math.pow(v, -1);
		return v;
	}

	public double getOptimumEval() {
		return 0.998003838;
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}

}
