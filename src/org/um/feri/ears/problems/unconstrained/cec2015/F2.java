package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

public class F2 extends CEC2015{
	
	public F2(int d) {
		super(d,2);

		name = "F02 Discus Function";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.discus_func(ds, numberOfDimensions, OShift, M, 1, 1);
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.discus_func(x, numberOfDimensions, OShift, M, 1, 1);
		return F;
	}

}
