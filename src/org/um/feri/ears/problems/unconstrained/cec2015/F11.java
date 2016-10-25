package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F11 extends CEC2015{
	
	public F11(int d) {
		super(d,11);

		name = "F11 Hybrid Function 2";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.hf03(ds, numberOfDimensions, OShift, M, SS, 1, 1);
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.hf03(x, numberOfDimensions, OShift, M, SS, 1, 1);
		return F;
	}
}
