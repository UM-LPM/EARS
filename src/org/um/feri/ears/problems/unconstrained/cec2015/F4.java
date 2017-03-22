package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F4 extends CEC2015 {

	public F4(int d) {
		super(d,4);

		name = "F04 Schwefel's Function";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.schwefel_func(x, numberOfDimensions, OShift, M, 1, 0);
		F+= 100 * func_num;
		return F;
	}
}
