package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F24 extends CEC2014 {
	
	public F24(int d) {
		super(d,24);

		name = "F24 Composition Function 2";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.cf02(x,numberOfDimensions,OShift,M,1);
		F+=2400.0;
		return F;
	}

}