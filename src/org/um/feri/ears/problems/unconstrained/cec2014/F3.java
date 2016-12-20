package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F3 extends CEC2014 {
	
	public F3(int d) {
		super(d,3);

		name = "F03 Discus Function";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.discus_func(ds,numberOfDimensions,OShift,M,1,1);
		F+=300.0;
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.discus_func(x,numberOfDimensions,OShift,M,1,1);
		F+=300.0;
		return F;
	}

}