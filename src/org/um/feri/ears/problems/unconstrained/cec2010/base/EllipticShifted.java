package org.um.feri.ears.problems.unconstrained.cec2010.base;

import java.util.List;

public class EllipticShifted{
	
	public int dim;
	public double[] shifted_optimum;
	
	public EllipticShifted(int d) {
		dim = d;
		shifted_optimum = new double[dim];
		for (int i=0; i<dim; i++){
			shifted_optimum[i] = 5;
		}
	}
	
	public double eval(double x[], int[] P, int start, int end) {
		double F = 0;
		
		double[] z = new double[dim];
		for (int i=start; i<end; i++){
			int j = P[i];
			z[i] = x[j] - shifted_optimum[j];	
			F += Math.pow(1000000, (i-1)/(dim-1))*z[i]*z[i];
		}
		
		return F;
	}
	
	public double eval(List<Double> x, int[] P, int start, int end) {
		double F = 0;
		
		double[] z = new double[dim];
		for (int i=start; i<end; i++){
			int j = P[i];
			z[i] = x.get(j) - shifted_optimum[j];	
			F += Math.pow(1000000, (i-1)/(dim-1))*z[i]*z[i];
		}
		
		return F;
	}

}