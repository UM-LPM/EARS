package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F29 extends CEC2014 {
	
	public F29(int d) {
		super(d,29);

		name = "F29 Composition Function 7";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.cf07(x,numberOfDimensions,OShift,M,SS,1);
		F+=2900.0;
		return F;
	}

}