/* Copyright 2009-2015 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.um.feri.ears.qualityIndicator;

import org.um.feri.ears.problems.moo.ParetoSolution;

/**
 * Computes the R3 indicator.  The R3 indicator is a utility ratio, or the
 * value of the R2 indicator divided by the reference set utility.  Values
 * range from {@code [-inf, inf]} with values nearer to {@code -inf} preferred.
 * <p>
 * References:
 * <ol>
 *   <li>Hansen, M. P. and A. Jaszkiewicz (1998).  Evaluating the Quality of
 *       Approximations to the Non-dominated Set.  IMM Technical Report
 *       IMM-REP-1998-7.
 * </ol>
 */
public class R3<T extends Number> extends RIndicator<T> {

	public R3(int num_obj, String file_name) {
		super(num_obj, file_name);
		name = "R3 indicator";
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
		
		double sum = 0.0;
		
		for (int i = 0; i < weights.length; i++) {
			double max1 = Double.NEGATIVE_INFINITY;
			double max2 = Double.NEGATIVE_INFINITY;
			
			for (double[] solution : normalizedApproximation) {
				max1 = Math.max(max1, utilityFunction.computeUtility(solution,
						weights[i]));
			}
			
			for (double[] solution : normalizedReference) {
				max2 = Math.max(max2, utilityFunction.computeUtility(solution,
						weights[i]));
			}
			
			sum += (max2 - max1) / (max2 + 1e-30);
		}
		
		return sum / weights.length;
	}

	@Override
	public IndicatorType getIndicatorType() {
		return IndicatorType.UNARY;
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
		return 0;
	}

}
