package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F6 extends CEC2014 {
	
	public F6(int d) {
		super(d,6);

		name = "F06 Weierstrass Function";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.weierstrass_func(ds,numberOfDimensions,OShift,M,1,1);
		F+=600.0;
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.weierstrass_func(x,numberOfDimensions,OShift,M,1,1);
		F+=600.0;
		return F;
	}

}
