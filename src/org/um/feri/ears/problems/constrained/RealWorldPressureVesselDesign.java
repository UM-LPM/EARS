package org.um.feri.ears.problems.constrained;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.Problem;

/**
* Real-World Problem:
* Pressure Vessel Design
* 
* @author Janez Krnc
* @version 1
* 
* <p>
* Goal: Minimize total cost (material, forming, welding) of the cylindrical pressure vessel
* Variables: Thickness of the shell (x1), thickness of the head (x2), inner radius (x3)
* </p>
*/
public class RealWorldPressureVesselDesign extends Problem {

	public RealWorldPressureVesselDesign() {
		super(4,4);
		minimize = true;
		max_constraints = new Double[numberOfConstraints];
		min_constraints = new Double[numberOfConstraints];
		count_constraints  = new Double[numberOfConstraints];
		sum_constraints  = new Double[numberOfConstraints];
		normalization_constraints_factor = new Double[numberOfConstraints];
		
		upperLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		lowerLimit = new ArrayList<Double>(Collections.nCopies(numberOfDimensions, 0.0));
		
		lowerLimit.set(0, 0.0);
		upperLimit.set(0, 99.0);
		
		lowerLimit.set(1, 0.0);
		upperLimit.set(1, 99.0);
		
		lowerLimit.set(2, 10.0);
		upperLimit.set(2, 200.0);
		
		lowerLimit.set(3, 10.0);
		upperLimit.set(3, 200.0);
		
		
		name = "PressureVesselDesign";
	}

	public double eval(double x[]) {
		double v = 0;
		v = 0.6244 * x[0] * x[2] * x[3] 
				+ 1.7781 * x[1] * Math.pow(x[2], 2)
				+ 3.1661 * Math.pow(x[0], 2) * x[3]
				+ 19.84 * Math.pow(x[0], 2) * x[2];
		return v;
	}

	public double[] calc_constraints(double x[]) {
		double[] g = new double[numberOfConstraints];
		g[0] = -x[0] + 0.0193 * x[2];
		g[1] = -x[2] + 0.00954 * x[2];
		g[2] = -Math.PI * Math.pow(x[2], 3) * x[3] - (4/3) * Math.PI * Math.pow(x[2], 3) + 1296000;
		g[3] = x[3] - 240;
		
		return g;
	}
	
	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
}
