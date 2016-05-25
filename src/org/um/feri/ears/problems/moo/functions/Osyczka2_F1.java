package org.um.feri.ears.problems.moo.functions;

import org.um.feri.ears.problems.moo.Objective;

public class Osyczka2_F1 extends Objective{

	@Override
	public double eval(double[] ds) {

		double x1,x2,x3,x4,x5;
		x1 = ds[0];
		x2 = ds[1];
		x3 = ds[2];
		x4 = ds[3];
		x5 = ds[4];

	    double f1 = - (25.0*(x1-2.0)*(x1-2.0) + 
	                  (x2-2.0)*(x2-2.0) + 
	                  (x3-1.0)*(x3-1.0) + 
	                  (x4-4.0)*(x4-4.0)+
	                  (x5-1.0)*(x5-1.0));
	    return f1;
	}

}
