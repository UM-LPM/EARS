package org.um.feri.analyse.EE.util;

public class Util {
	/**
	 * In case where all dimension have same epsilon. If not read vector from input, or set it programaticaly!
	 * 
	 * @param dimension
	 * @param epsilon
	 * @return
	 */
	public static double[] generateEpsilonVector(int dimension, double epsilon) {
		 double[] t = new  double[dimension];
		 for (int i=0; i<dimension; i++) {
			 t[i] = epsilon;
		 }
		 return t;
	}
}
