package org.um.feri.ears.problems.unconstrained.cec2010.base;

import java.util.List;

import org.um.feri.ears.problems.unconstrained.cec.Constants;

public class AckleyShifted{
	
	public int dim;
	public double[] shifted_optimum;

	public AckleyShifted(int d) {
		dim = d;
		shifted_optimum = new double[dim];
		for (int i=0; i<dim; i++){
			shifted_optimum[i] = 5;
		}
	}
	
	public double eval(double x[], int[] P, int start, int end) {
		double F=0;
		double sphere=0;
		double cos=0;
		int dim = x.length;
		double[] z = new double[dim];
		for (int i=start; i<end; i++){
			int j = P[i];
			z[i] = x[j] - shifted_optimum[j];	
			sphere+= z[i]*z[i];
			cos+=Math.cos(2*Math.PI*z[i]);
		}
		sphere = -0.2*Math.sqrt(sphere / dim);
		cos /= dim;
		//F = -20 * Math.exp(sphere) - Math.exp(cos) + 20 + Constants.E;
		F = Math.E - 20.0 * Math.exp(sphere) - Math.exp(cos) + 20.0;
		return F;
	}

	public double eval(List<Double> ds, int[] P, int start, int end) {
		double F=0;
		double sphere=0;
		double cos=0;
		int dim = ds.size();
		double[] z = new double[dim];
		for (int i=start; i<end; i++){
			int j = P[i];
			z[i] = ds.get(j) - shifted_optimum[j];	
			sphere+= z[i]*z[i];
			cos+=Math.cos(2*Math.PI*z[i]);
		}
		sphere = -0.2*Math.sqrt(sphere / dim);
		cos /= dim;
		//F = -20 * Math.exp(sphere) - Math.exp(cos) + 20 + Constants.E;
		F = Math.E - 20.0 * Math.exp(sphere) - Math.exp(cos) + 20.0;
		return F;
	}
}