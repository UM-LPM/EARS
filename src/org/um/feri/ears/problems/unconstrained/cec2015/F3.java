package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F3 extends CEC2015{
	
	public F3(int d) {
		super(d,3);

		name = "F03 Weierstrass's Function";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.weierstrass_func(ds, numberOfDimensions, OShift, M, 1, 1);
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.weierstrass_func(x, numberOfDimensions, OShift, M, 1, 1);
		return F;
	}

}
