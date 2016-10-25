package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F27 extends CEC2014 {
	
	public F27(int d) {
		super(d,27);

		name = "F27 Composition Function 5";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.cf05(ds,numberOfDimensions,OShift,M,1);
		F+=2700.0;
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.cf05(x,numberOfDimensions,OShift,M,1);
		F+=2700.0;
		return F;
	}

}