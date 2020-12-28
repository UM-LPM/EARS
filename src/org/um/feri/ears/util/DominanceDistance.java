package org.um.feri.ears.util;

public class DominanceDistance implements PointDistance {

	@Override
	public double compute(double[] a, double[] b) {
		if (a == null) {
			System.err.println("The first point is null");
			return Double.MAX_VALUE;
		} else if (b == null) {
			System.err.println("The second point is null");
			return Double.MAX_VALUE;
		} else if (a.length != b.length) {
			System.err.println("The dimensions of the points are different: " + a.length + ", " + b.length);
			return Double.MAX_VALUE;
		}

		double distance = 0.0;

		for (int i = 0; i < a.length; i++) {
			// ignore the dominating objective (smaller value in the approximation set) by subtracting the objective value of approximation set with the objective value of the reference set
			double max = Math.max(b[i] - a[i], 0.0); 
			distance += Math.pow(max, 2);
		}
		return Math.sqrt(distance);
	}
}
