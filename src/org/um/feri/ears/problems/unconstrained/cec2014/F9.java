package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F9 extends CEC2014 {
	
	public F9(int d) {
		super(d,9);

		name = "F09 Rastrigin Function";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.rastrigin_func(ds,numberOfDimensions,OShift,M,1,1);
		F+=900.0;
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.rastrigin_func(x,numberOfDimensions,OShift,M,1,1);
		F+=900.0;
		return F;
	}

}
