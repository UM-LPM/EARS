//  Spread.java
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

package org.um.feri.ears.qualityIndicator;

import java.util.Arrays;

import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOProblemBase;

/**
 * This class implements the spread quality indicator. 
 * This metric is only applicable to two bi-objective problems.
 * Reference: Deb, K., Pratap, A., Agarwal, S., Meyarivan, T.: A fast and 
 *            elitist multiobjective genetic algorithm: NSGA-II. IEEE Trans. 
 *            on Evol. Computation 6 (2002) 182-197
 */
public class Spread<T extends Number> extends QualityIndicator<T>{

	/**
	 * Constructor. Creates a new instance of a Spread object
	 */
	public Spread(int num_obj, String file_name) {
		super(num_obj, file_name, (ParetoSolution<T>) getReferenceSet(file_name));
		name="Spread";
	}

	@Override
	public double evaluate(ParetoSolution<T> population) {
		
		/**
		 * Stores the normalized approximation set.
		 */
		double[][] normalizedApproximation;

		normalizedApproximation = MetricsUtil.getNormalizedFront(population.writeObjectivesToMatrix(), maximumValue, minimumValue);

		// Sort normalizedFront and normalizedParetoFront;
		Arrays.sort(normalizedApproximation, new LexicoGraphicalComparator());
		Arrays.sort(normalizedReference, new LexicoGraphicalComparator());

		int numberOfPoints = normalizedApproximation.length;
		// int numberOfTruePoints = normalizedParetoFront.length;

		// Compute df and dl (See specifications in Deb's description of the metric)
		double df;
		double dl;
		double mean;
		double diversitySum;
		try {
			df = MetricsUtil.distance(normalizedApproximation[0], normalizedReference[0]);
			dl = MetricsUtil.distance(normalizedApproximation[normalizedApproximation.length - 1], normalizedReference[normalizedReference.length - 1]);

			mean = 0.0;
			diversitySum = df + dl;

			// Calculate the mean of distances between points i and (i - 1). (the poins are in lexicografical order)
			for (int i = 0; i < (normalizedApproximation.length - 1); i++) {
				mean += MetricsUtil.distance(normalizedApproximation[i], normalizedApproximation[i + 1]);
			}


			mean = mean / (double) (numberOfPoints - 1);

			// If there are more than a single point, continue computing the metric. In other case, return the worse value (1.0, see metric's description).
			if (numberOfPoints > 1) {
				try {
					for (int i = 0; i < (numberOfPoints - 1); i++) {
						diversitySum += Math.abs(MetricsUtil.distance(normalizedApproximation[i],
								normalizedApproximation[i + 1]) - mean);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return diversitySum / (df + dl + (numberOfPoints - 1) * mean);
			} else
				return 1.0;
		} catch (Exception e1) {
			e1.printStackTrace();
			System.err.println(e1.getMessage());
		}
		return 1.0;
	}

	@Override
	public boolean isMin() {
		return true;
	}

	@Override
	public IndicatorType getIndicatorType() {
		return QualityIndicator.IndicatorType.Unary;
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
