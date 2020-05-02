package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F13 extends CEC2014 {
	
	public F13(int d) {
		super(d,13);

		name = "F13 HappyCat function";
	}

	@Override
	public double eval(double[] x) {
		double F;
		F = Functions.happycat_func(x,numberOfDimensions,OShift,M,1,1);
		F+=1300.0;
		return F;
	}

}
