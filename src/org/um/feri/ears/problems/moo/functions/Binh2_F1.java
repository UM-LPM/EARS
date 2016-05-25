package org.um.feri.ears.problems.moo.functions;

import org.um.feri.ears.problems.moo.Objective;

public class Binh2_F1 extends Objective{

	@Override
	public double eval(double[] ds) {
		double f1 = 4.0*Math.pow(ds[0], 2.0) + 4.0*Math.pow(ds[1], 2.0);
		return f1;
	}
}
