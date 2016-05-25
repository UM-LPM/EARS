package org.um.feri.ears.problems.moo.functions;

import org.um.feri.ears.problems.moo.Objective;

public class Golinski_F2 extends Objective{

	@Override
	public double eval(double[] ds) {
		double x2,x3,x4,x6;

		x2 = ds[1];
		x3 = ds[2];
		x4 = ds[3];
		x6 = ds[5];

		double aux = 745.0 * x4 / (x2 * x3);
		double f2 = Math.sqrt((aux*aux)+1.69e7) / (0.1*x6*x6*x6);

		return f2;
	}

}
