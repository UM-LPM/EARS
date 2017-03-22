package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F28 extends CEC2014 {
	
	public F28(int d) {
		super(d,28);

		name = "F28 Composition Function 6";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.cf06(x,numberOfDimensions,OShift,M,1);
		F+=2800.0;
		return F;
	}

}