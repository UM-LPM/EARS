package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

public class F13 extends CEC2015{
	
	public F13(int d) {
		super(d,13);

		name = "F13 Composition Function 1";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.cf01(ds, numberOfDimensions, OShift, M, 1);
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.cf01(x, numberOfDimensions, OShift, M, 1);
		return F;
	}
}
