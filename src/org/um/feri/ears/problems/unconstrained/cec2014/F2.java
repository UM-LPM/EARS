package org.um.feri.ears.problems.unconstrained.cec2014;

import java.util.List;

import org.um.feri.ears.problems.unconstrained.cec.Functions;

public class F2 extends CEC2014 {
	
	public F2(int d) {
		super(d,2);

		name = "F02 Bent Cigar Function";
	}

	@Override
	public double eval(List<Double> ds) {
		double F;
		F = Functions.bent_cigar_func(ds,numberOfDimensions,OShift,M,1,1);
		F+=200.0;
		return F;
	}
	
	public double eval(double x[]) {
		double F;
		F = Functions.bent_cigar_func(x,numberOfDimensions,OShift,M,1,1);
		F+=200.0;
		return F;
	}

}
