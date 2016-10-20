package org.um.feri.ears.problems.unconstrained.cec2015;

import java.util.List;

public class F8 extends CEC2015 {
	
	public F8(int d) {
		super(d,8);

		name = "F08 Griewank-Rosenbrock Function";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.grie_rosen_func(ds, numberOfDimensions, OShift, M, 1, 1);
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.grie_rosen_func(x, numberOfDimensions, OShift, M, 1, 1);
		return F;
	}

}
