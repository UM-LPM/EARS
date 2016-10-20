package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;


public class F6 extends CEC2015{
	
	public F6(int d) {
		super(d,6);

		name = "F06 HappyCat Function";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.happycat_func(ds, numberOfDimensions, OShift, M, 1, 1);
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.happycat_func(x, numberOfDimensions, OShift, M, 1, 1);
		return F;
	}

}
