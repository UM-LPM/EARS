package org.um.feri.ears.util;

public class ManhattanDistance implements PointDistance {

	@Override
	public double compute(double[] a, double[] b) throws Exception {
		if (a == null) {
			throw new Exception("The first point is null");
		} else if (b == null) {
			throw new Exception("The second point is null");
		} else if (a.length != b.length) {
			throw new Exception("The dimensions of the points are different: " + a.length + ", " + b.length);
		}

		double distance = 0.0;

		for (int i = 0; i < a.length; i++) {
			distance += Math.abs(a[i] - b[i]);
		}
		return distance;

	}
}