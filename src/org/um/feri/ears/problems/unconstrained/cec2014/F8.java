package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F8 extends CEC2014 {
	
	public F8(int d) {
		super(d,8);

		name = "F08 Rastrigin Function";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.rastrigin_func(x,numberOfDimensions,OShift,M,1,0);
		F+=800.0;
		return F;
	}

}