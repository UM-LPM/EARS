package org.um.feri.ears.problems.unconstrained.cec2010.base;

import java.util.List;

public class RosenbrockShifted{
	
	public int dim;
	public double[] shifted_optimum;

	public RosenbrockShifted(int d) {
		dim = d;
		shifted_optimum = new double[dim];
		for (int i=0; i<dim; i++){
			shifted_optimum[i] = 1.3;
		}
	}
	
	public double eval(double x[], int[] P, int start, int end) {
		double F=0;
		double[] z = new double[dim];
		int k;
		for (int i=0; i<dim-1; i++) {
			k = P[i];
			z[i] = x[k] - shifted_optimum[k];
			F += 100*Math.pow((z[i]*z[i]-z[i+1]),2) + Math.pow(z[i]-1,2);
		}
		
		return F;
	}

	public double eval(List<Double> ds, int[] P, int start, int end) {
		double F=0;
		double[] z = new double[dim];
		int k;
		for (int i=0; i<dim-1; i++) {
			k = P[i];
			z[i] = ds.get(k) - shifted_optimum[k];
			F += 100*Math.pow((z[i]*z[i]-z[i+1]),2) + Math.pow(z[i]-1,2);
		}
		
		return F;
	}
}