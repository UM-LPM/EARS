package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F18 extends CEC2014 {
	
	public F18(int d) {
		super(d,18);

		name = "F18 Hybrid Function 2";
	}

	@Override
	public double eval(double[] x) {
		double F;
		F = Functions.hf02(x,numberOfDimensions,OShift,M,SS,1,1);
		F+=1800.0;
		return F;
	}

}