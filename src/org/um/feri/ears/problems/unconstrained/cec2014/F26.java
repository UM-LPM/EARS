package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F26 extends CEC2014 {
	
	public F26(int d) {
		super(d,26);

		name = "F26 Composition Function 4";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.cf04(x,numberOfDimensions,OShift,M,1);
		F+=2600.0;
		return F;
	}

}