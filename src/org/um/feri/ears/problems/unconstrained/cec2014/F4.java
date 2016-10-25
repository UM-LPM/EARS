package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F4 extends CEC2014 {
	
	public F4(int d) {
		super(d,4);

		name = "F04 Rosenbrock Function";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.rosenbrock_func(ds,numberOfDimensions,OShift,M,1,1);
		F+=400.0;
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.rosenbrock_func(x,numberOfDimensions,OShift,M,1,1);
		F+=400.0;
		return F;
	}

}
