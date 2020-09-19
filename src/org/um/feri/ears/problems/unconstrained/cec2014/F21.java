package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F21 extends CEC2014 {
	
	public F21(int d) {
		super(d,21);

		name = "F21 Hybrid Function 5";
	}

	@Override
	public double eval(double[] x) {
		double F;
		F = Functions.hf05(x,numberOfDimensions,OShift,M,SS,1,1);
		F+=2100.0;
		return F;
	}

}