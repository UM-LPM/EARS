package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.unconstrained.cec.Functions;


public class F1 extends CEC2015  {
	
	
	public F1(int d) {
		super(d,1);

		name = "F01 Bent Cigar";
	}

	@Override
	public double eval(double[] x) {
		double F;
		F = Functions.bent_cigar_func(x, numberOfDimensions, OShift, M, 1, 1);
		F+= 100 * func_num;
		return F;
	}
	
}
