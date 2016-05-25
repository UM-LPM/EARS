package org.um.feri.ears.problems.unconstrained.cec2010.base;


public class Sphere{

	public double eval(double x[]) {
		double F=0;
		int dim = x.length;
		for (int i=0; i<dim; i++) {
			F+=x[i]*x[i];
		}
		return F;
	}

}