package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F1 extends CEC2014 {
	
	public F1(int d) {
		super(d,1);

		name = "F01 Ellips Function";
	}

	@Override
	public double eval(double[] x) {
		double F;
		F = Functions.ellips_func(x,numberOfDimensions,OShift,M,1,1);
		F+=100.0;
		return F;
	}
}
