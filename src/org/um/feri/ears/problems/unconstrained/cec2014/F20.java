package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F20 extends CEC2014 {
	
	public F20(int d) {
		super(d,20);

		name = "F20 Hybrid Function 4";
	}

	@Override
	public double eval(double[] x) {
		double F;
		F = Functions.hf04(x,numberOfDimensions,OShift,M,SS,1,1);
		F+=2000.0;
		return F;
	}

}