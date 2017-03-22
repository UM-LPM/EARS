package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F17 extends CEC2014 {
	
	public F17(int d) {
		super(d,17);

		name = "F17 Hybrid Function 1";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.hf01(x,numberOfDimensions,OShift,M,SS,1,1);
		F+=1700.0;
		return F;
	}

}