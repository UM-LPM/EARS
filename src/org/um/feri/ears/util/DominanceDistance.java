//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.um.feri.ears.util;


/**
 * Computes the distance between two points a y b according to the dominance relationship. Point a
 * is supposed to be point of the Pareto front
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class DominanceDistance implements PointDistance {

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
			// ignore the dominating objective (smaller value in the approximation set) by subtracting the objective value of approximation set with the objective value of the reference set
			double max = Math.max(b[i] - a[i], 0.0); 
			distance += Math.pow(max, 2);
		}
		return Math.sqrt(distance);
	}
}
