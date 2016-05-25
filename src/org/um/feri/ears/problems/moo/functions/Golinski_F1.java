package org.um.feri.ears.problems.moo.functions;

import org.um.feri.ears.problems.moo.Objective;

public class Golinski_F1 extends Objective{

	@Override
	public double eval(double[] ds) {
		double x1,x2,x3,x4,x5,x6,x7;
	    x1 = ds[0];
	    x2 = ds[1];
	    x3 = ds[2];
	    x4 = ds[3];
	    x5 = ds[4];
	    x6 = ds[5];
	    x7 = ds[6];
	        
	    double f1 = 0.7854 * x1 *x2 *x2 * ((10*x3*x3)/3.0 + 14.933*x3 - 43.0934) - 
	                1.508*x1*(x6*x6 + x7*x7)+7.477*(x6*x6*x6 + x7*x7*x7) + 
	                0.7854*(x4*x6*x6 + x5*x7*x7);
	    return f1;
	}

}
