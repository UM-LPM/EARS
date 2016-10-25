package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F5 extends CEC2014 {
	
	public F5(int d) {
		super(d,5);

		name = "F05 Ackley Function";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.ackley_func(ds,numberOfDimensions,OShift,M,1,1);
		F+=500.0;
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.ackley_func(x,numberOfDimensions,OShift,M,1,1);
		F+=500.0;
		return F;
	}

}
