package org.um.feri.ears.problems.moo.functions;

import org.um.feri.ears.problems.moo.Objective;

public class Osyczka2_F2 extends Objective{

	@Override
	public double eval(double[] ds) {
		double x1,x2,x3,x4,x5,x6;
		x1 = ds[0];
		x2 = ds[1];
		x3 = ds[2];
		x4 = ds[3];
		x5 = ds[4];
		x6 = ds[5];
		
	    double f2 = x1*x1 + x2*x2 + x3*x3 + x4*x4 + x5*x5 + x6*x6;
		return f2;
	}

}
