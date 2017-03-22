package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F25 extends CEC2014 {
	
	public F25(int d) {
		super(d,25);

		name = "F25 Composition Function 3";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.cf03(x,numberOfDimensions,OShift,M,1);
		F+=2500.0;
		return F;
	}

}