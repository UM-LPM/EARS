package org.um.feri.ears.problems.unconstrained.cec2010.base;

import java.util.List;

public class SchwefelShifted{
	
	public int dim;
	public double[] shifted_optimum;

	public SchwefelShifted(int d) {
		dim = d;
		shifted_optimum = new double[dim];
		for (int i=0; i<dim; i++){
			shifted_optimum[i] = 0.8;
		}
	}
	
	public double eval(double x[], int[] P, int start, int end) {
		double F=0;
		int k;
		double[] z = new double[dim];
		for (int i=start; i<end; i++){
			double ff = 0;
			for (int j=start; j<i; j++){
				k = P[j];
				z[i] = x[k] - shifted_optimum[k];
				ff += z[i];
			}
			F += ff*ff;
		}
		
		return F;
	}

	public double eval(List<Double> ds, int[] P, int start, int end) {
		double F=0;
		int k;
		double[] z = new double[dim];
		for (int i=start; i<end; i++){
			double ff = 0;
			for (int j=start; j<i; j++){
				k = P[j];
				z[i] = ds.get(k) - shifted_optimum[k];
				ff += z[i];
			}
			F += ff*ff;
		}
		
		return F;
	}
}