package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F23 extends CEC2014 {
	
	public F23(int d) {
		super(d,23);

		name = "F23 Composition Function 1";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.cf01(x,numberOfDimensions,OShift,M,1);
		F+=2300.0;
		return F;
	}

}