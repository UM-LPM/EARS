package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F15 extends CEC2015{
	
	public F15(int d) {
		super(d,15);

		name = "F15 Composition Function 3";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.cf05(x, numberOfDimensions, OShift, M, 1);
		F+= 100 * func_num;
		return F;
	}
}
