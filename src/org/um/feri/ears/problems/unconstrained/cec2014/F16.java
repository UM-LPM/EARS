package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F16 extends CEC2014 {
	
	public F16(int d) {
		super(d,16);

		name = "F16 Expanded Scaffer's function";
	}

	@Override
	public double eval(double[] x) {
		double F;
		F = Functions.escaffer6_func(x,numberOfDimensions,OShift,M,1,1);
		F+=1600.0;
		return F;
	}

}