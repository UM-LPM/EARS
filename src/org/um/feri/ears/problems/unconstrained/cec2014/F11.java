package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F11 extends CEC2014 {
	
	public F11(int d) {
		super(d,11);

		name = "F11 Schwefel function";
	}

	@Override
	public double eval(double[] x) {
		double F;
		F = Functions.schwefel_func(x,numberOfDimensions,OShift,M,1,1);
		F+=1100.0;
		return F;
	}

}
