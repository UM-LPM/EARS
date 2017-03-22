package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F14 extends CEC2014 {
	
	public F14(int d) {
		super(d,14);

		name = "F14 HGBat function";
	}

	@Override
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.hgbat_func(x,numberOfDimensions,OShift,M,1,1);
		F+=1400.0;
		return F;
	}

}