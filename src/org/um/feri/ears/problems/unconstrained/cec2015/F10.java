package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F10 extends CEC2015 {
	
	public F10(int d) {
		super(d,10);

		name = "F10 Hybrid Function 1";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.hf01(x, numberOfDimensions, OShift, M, SS, 1, 1);
		F+= 100 * func_num;
		return F;
	}
}
