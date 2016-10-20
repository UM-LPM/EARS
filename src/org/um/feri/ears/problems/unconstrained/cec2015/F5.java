package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

public class F5 extends CEC2015{
	
	public F5(int d) {
		super(d,5);

		name = "F05 Katsuura Function";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.katsuura_func(ds, numberOfDimensions, OShift, M, 1, 1);
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.katsuura_func(x, numberOfDimensions, OShift, M, 1, 1);
		return F;
	}

}
