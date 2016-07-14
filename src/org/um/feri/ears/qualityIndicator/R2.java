//  R2.java
//
//  Author:
//       Juan J. Durillo <juanjo.durillo@gmail.com>
//
//  Copyright (c) 2013 Juan J. Durillo
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
package org.um.feri.ears.qualityIndicator;

import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOProblemBase;

/**
 * Computes the R2 indicator.  The R2 indicator is the expected utility
 * evaluated across a set of uniformly-weighted utility functions.  R2 is
 * weakly compatible with the outperformance relation for any set of utility
 * functions.  Values are normalized between {@code [-1, 1]} with {@code -1}
 * preferred.
 * <p>
 * References:
 * <ol>
 *   <li>Hansen, M. P. and A. Jaszkiewicz (1998).  Evaluating the Quality of
 *       Approximations to the Non-dominated Set.  IMM Technical Report
 *       IMM-REP-1998-7.
 * </ol>
 */
public class R2<T extends Number> extends RIndicator<T>{

	public R2(int num_obj, String file_name) {
		super(num_obj, file_name);
		name = "R2 indicator";
		this.utilityFunction = new ChebychevUtility();
		this.num_obj = num_obj;
		try {
			weights = generateUniformWeights(getDefaultSubdivisions(num_obj), num_obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public double evaluate(ParetoSolution<T> population) {
		
		/**
		 * Stores the normalized approximation set.
		 */
		double[][] normalizedApproximation;

		normalizedApproximation = MetricsUtil.getNormalizedFront(population.writeObjectivesToMatrix(), maximumValue, minimumValue);
		
		return expectedUtility(normalizedReference) - expectedUtility(normalizedApproximation);
	}

	@Override
	public IndicatorType getIndicatorType() {
		return IndicatorType.Unary;
	}

	@Override
	public boolean isMin() {
		return true;
	}

	@Override
	public boolean requiresReferenceSet() {
		return true;
	}

	@Override
	public int compare(ParetoSolution<T> front1, ParetoSolution<T> front2, Double epsilon) {
		// TODO Auto-generated method stub
		return 0;
	}

}
