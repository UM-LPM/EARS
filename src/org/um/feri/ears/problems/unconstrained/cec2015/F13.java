package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F13 extends CEC2015{
	
	public F13(int d) {
		super(d,13);

		name = "F13 Composition Function 1";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.cf01(x, numberOfDimensions, OShift, M, 1);
		F+= 100 * func_num;
		return F;
	}
}
