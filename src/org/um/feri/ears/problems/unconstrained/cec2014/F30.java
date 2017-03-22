package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F30 extends CEC2014 {
	
	public F30(int d) {
		super(d,30);

		name = "F30 Composition Function 8";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.cf08(x,numberOfDimensions,OShift,M,SS,1);
		F+=3000.0;
		return F;
	}

}