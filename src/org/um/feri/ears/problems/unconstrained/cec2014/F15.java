package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F15 extends CEC2014 {
	
	public F15(int d) {
		super(d,15);

		name = "F15 Griewank-Rosenbrock function";
	}

	@Override
	public double eval(double[] x) {
		double F;
		F = Functions.grie_rosen_func(x,numberOfDimensions,OShift,M,1,1);
		F+=1500.0;
		return F;
	}

}