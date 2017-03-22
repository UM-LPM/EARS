package org.um.feri.ears.problems.unconstrained.cec2010.base;

import java.util.List;

public class EllipticRotated{
	
	public int dim;
	public double[] shifted_optimum;
	

	public EllipticRotated(int d){
		dim = d;
		shifted_optimum = new double[dim];
		for (int i=0; i<dim; i++){
			shifted_optimum[i] = 5;
		}
	}
	
	public double eval(double x[], int[] P, int start, int end, double[][] rot_matrix) {
		double F = 0;
		int dim = x.length;
		double[] z = new double[dim];
		int k;
		for (int i=start; i<end; i++){
			k = P[i];
			z[i] = x[k] - shifted_optimum[k];
		}
		z = multiply(z, rot_matrix, start, end);
		
		for (int i=start; i<end; i++){	
			F += Math.pow(1000000, i/(dim-1))*z[i]*z[i];
		}
		
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
		int k;
		for (int i=start; i<end; i++){
			k = P[i];
			z[i] = ds.get(k) - shifted_optimum[k];
		}
		z = multiply(z, rot_matrix, start, end);
		
		for (int i=start; i<end; i++){	
			F += Math.pow(1000000, i/(dim-1))*z[i]*z[i];
		}
		
		return F;
	}

}