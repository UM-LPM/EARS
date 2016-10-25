package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F12 extends CEC2014 {
	
	public F12(int d) {
		super(d,12);

		name = "F12 Katsuura function";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.katsuura_func(ds,numberOfDimensions,OShift,M,1,1);
		F+=1200.0;
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.katsuura_func(x,numberOfDimensions,OShift,M,1,1);
		F+=1200.0;
		return F;
	}

}
