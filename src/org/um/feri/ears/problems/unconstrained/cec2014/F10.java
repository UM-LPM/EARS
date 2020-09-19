package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F10 extends CEC2014 {
	
	public F10(int d) {
		super(d,10);

		name = "F10 Schwefel function";
	}

	@Override
	public double eval(double[] x) {
		double F;
		F = Functions.schwefel_func(x,numberOfDimensions,OShift,M,1,0);
		F+=1000.0;
		return F;
	}
}
