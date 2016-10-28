//  WFG.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
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
package org.um.feri.ears.problems.moo.wfg;

import java.util.ArrayList;
import org.um.feri.ears.problems.moo.DoubleMOProblem;

/**
 * Implements a reference abstract class for all wfg org.uma.test problem
 * Reference: Simon Huband, Luigi Barone, Lyndon While, Phil Hingston
 * A Scalable Multi-objective Test Problem Toolkit.
 * Evolutionary Multi-Criterion Optimization:
 * Third International Conference, EMO 2005.
 * Proceedings, volume 3410 of Lecture Notes in Computer Science
 * 
 * Recommended values for k and l:
 *  Whilst it is largely a matter of choice, letting l=20 should be sufficient for the number of distance-related parameters.
 *  Whilst suggesting a value for k, the number of position-related parameters, is more difficult, we suggest the following:
    - If M == 2, then let l == 4. Otherwise, if M >= 3, let l == 2*(M-1). 
      This ensures there is always at least four position-related parameters, 
      and never the absolute minimum number of position-related parameters. 
      For problems exploring a large number of objectives, it may be desirable to 
      just use the minimum number of position-related parameters.
 */
public abstract class WFG extends DoubleMOProblem {

  /**
   * stores a epsilon default value
   */
  private final double epsilon = (double) 1e-7;

  protected int k;
  protected int m;
  protected int l;
  protected int[] a;
  protected int[] s;
  protected int d = 1;
  
  /**
   * Constructor
   * Creates a wfg problem
   *
   * @param k            position-related parameters
   * @param l            distance-related parameters
   * @param M            Number of objectives
   */
  public WFG(Integer k, Integer l, Integer M) {
	super(k + l, 0, M);
    
	benchmarkName = "WFG";
	
	this.k = k;
    this.l = l;
    this.m = M;
    
	upperLimit = new ArrayList<Double>(numberOfDimensions);
	lowerLimit = new ArrayList<Double>(numberOfDimensions);


	for (int i = 0; i < numberOfDimensions; i++) {
		lowerLimit.add(0.0);
		upperLimit.add(2.0*(i+1));
	}
    
  }

  /**
   * Gets the x vector
   */
  public double[] calculateX(double[] t) {
	  double[] x = new double[m];

    for (int i = 0; i < m - 1; i++) {
      x[i] = Math.max(t[m - 1], a[i]) * (t[i] - (double) 0.5) + (double) 0.5;
    }

    x[m - 1] = t[m - 1];

    return x;
  }

  /**
   * Normalizes a vector (consulte wfg toolkit reference)
   */
  public double[] normalise(double[] z) {
	  double[] result = new double[z.length];

    for (int i = 0; i < z.length; i++) {
    	double bound = (double) 2.0 * (i + 1);
      result[i] = z[i] / bound;
      result[i] = correctTo01(result[i]);
    }

    return result;
  }

  /**
   */
  public double correctTo01(double a) {
	double min = (double) 0.0;
	double max = (double) 1.0;

	double minEpsilon = min - epsilon;
	double maxEpsilon = max + epsilon;

    if ((a <= min && a >= minEpsilon) || (a >= min && a <= minEpsilon)) {
      return min;
    } else if ((a >= max && a <= maxEpsilon) || (a <= max && a >= maxEpsilon)) {
      return max;
    } else {
      return a;
    }
  }

  /**
   * Gets a subvector of a given vector
   * (Head inclusive and tail inclusive)
   *
   * @param z the vector
   * @return the subvector
   */
  public double[] subVector(double[] z, int head, int tail) {
    int size = tail - head + 1;
    double[] result = new double[size];

    System.arraycopy(z, head, result, head - head, tail + 1 - head);

    return result;
  }
}
