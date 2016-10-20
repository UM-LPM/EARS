package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

public class F15 extends CEC2015{
	
	public F15(int d) {
		super(d,15);

		name = "F15 Composition Function 3";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.cf05(ds, numberOfDimensions, OShift, M, 1);
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.cf05(x, numberOfDimensions, OShift, M, 1);
		return F;
	}
}
