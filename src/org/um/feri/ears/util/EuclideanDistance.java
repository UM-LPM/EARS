package org.um.feri.ears.util;

/**
* Computes the Euclidean distance between two points
*/
public class EuclideanDistance implements PointDistance {

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
			distance += Math.pow(a[i] - b[i], 2.0);
		}
		return Math.sqrt(distance);

	}
}
