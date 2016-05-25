package org.um.feri.ears.problems.moo.functions;

import org.um.feri.ears.problems.moo.Objective;

public class Binh2_F2 extends Objective{

	@Override
	public double eval(double[] ds) {
		double f2 = Math.pow(ds[0] - 5.0, 2.0) + Math.pow(ds[1] - 5.0, 2.0);
		return f2;
	}

}
