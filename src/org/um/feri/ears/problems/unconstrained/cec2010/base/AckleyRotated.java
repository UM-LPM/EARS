package org.um.feri.ears.problems.unconstrained.cec2010.base;

import java.util.List;

public class AckleyRotated{
	
	public int dim;
	public double[] shifted_optimum;
	
	public AckleyRotated(int d){
		dim = d;
		shifted_optimum = new double[dim];
		for (int i=0; i<dim; i++){
			shifted_optimum[i] = 2.7;
		}
	}
	
	public double eval(double x[], int[] P, int start, int end, double[][] rot_matrix) {
		double F = 0;
		int dim = x.length;
		double[] z = new double[dim];
		
		for (int i=start; i<end; i++){
			int j = P[i];
			z[i] = x[j] - shifted_optimum[j];
		}
		z = multiply(z, rot_matrix, start, end);
		
		double sphere=0;
		double cos=0;
		
		for (int i=start; i<end; i++){
			sphere+=z[i]*z[i];
			cos+=Math.cos(2*Math.PI*z[i]);
		}
		F = -20*Math.exp(-0.2*Math.sqrt(1/dim * sphere)) - Math.exp(cos) + 20 + Math.E;
		
		return F;
	}
	
	public double[] multiply(double[] v, double[][] m, int start, int end){
		double newv[] = new double[dim];
		newv = v.clone();
		double sum = 0;
		for (int j=start; j<end; j++){
			for (int i=start; i<end; i++){
				sum = sum + v[i]*m[i][j];
			}
			newv[j] = sum;
			sum = 0;
		}
		return newv;
	}

	public double eval(List<Double> ds, int[] P, int start, int end, double[][] rot_matrix) {
		double F = 0;
		int dim = ds.size();
		double[] z = new double[dim];
		
		for (int i=start; i<end; i++){
			int j = P[i];
			z[i] = ds.get(j) - shifted_optimum[j];
		}
		z = multiply(z, rot_matrix, start, end);
		
		double sphere=0;
		double cos=0;
		
		for (int i=start; i<end; i++){
			sphere+=z[i]*z[i];
			cos+=Math.cos(2*Math.PI*z[i]);
		}
		F = -20*Math.exp(-0.2*Math.sqrt(1/dim * sphere)) - Math.exp(cos) + 20 + Math.E;
		
		return F;
	}

}