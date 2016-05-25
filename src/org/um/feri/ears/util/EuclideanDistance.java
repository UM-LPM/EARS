//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU Lesser General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.um.feri.ears.util;

/**
* Computes the Euclidean distance between two points
*
* @author Antonio J. Nebro <antonio@lcc.uma.es>
*/
public class EuclideanDistance implements PointDistance {

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
			distance += Math.pow(a[i] - b[i], 2.0);
		}
		return Math.sqrt(distance);

	}
}
