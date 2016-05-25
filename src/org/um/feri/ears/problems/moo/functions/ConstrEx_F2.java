package org.um.feri.ears.problems.moo.functions;

import org.um.feri.ears.problems.moo.Objective;

public class ConstrEx_F2 extends Objective {

	@Override
	public double eval(double[] ds) {
		double f2 = (1.0 + ds[1])/ds[0];
		return f2;
	}

}
