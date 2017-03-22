package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F22 extends CEC2014 {
	
	public F22(int d) {
		super(d,22);

		name = "F22 Hybrid Function 6";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.hf06(x,numberOfDimensions,OShift,M,SS,1,1);
		F+=2200.0;
		return F;
	}

}