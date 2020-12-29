//  GenerationalDistance.java
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

import org.um.feri.ears.problems.moo.ParetoSolution;

/**
 * This class implements the generational distance indicator. It can be used also 
 * as a command line by typing: 
 * "java jmetal.qualityIndicator.GenerationalDistance <solutionFrontFile>  
 * <trueFrontFile> <numberOfObjectives>"
 * Reference: Van Veldhuizen, D.A., Lamont, G.B.: Multiobjective Evolutionary 
 *            Algorithm Research: A History and Analysis. 
 *            Technical Report TR-98-03, Dept. Elec. Comput. Eng., Air Force 
 *            Inst. Technol. (1998)
 */
public class GenerationalDistance<T extends Number> extends QualityIndicator<T>{

	static final double pow = 2.0; // pow. This is the pow used for the distances

	/**
	 * Constructor. Creates a new instance of the generational distance metric.
	 */
	public GenerationalDistance(int num_obj, String file_name) {
		super(num_obj, file_name, (ParetoSolution<T>) getReferenceSet(file_name));
		name = "Generational Distance";
	}

	@Override
	public double evaluate(ParetoSolution<T> paretoFrontApproximation) {
		
		/**
		 * Stores the normalized approximation set.
		 */
		double[][] normalizedApproximation;

		normalizedApproximation = QualityIndicatorUtil.getNormalizedFront(paretoFrontApproximation.writeObjectivesToMatrix(), maximumValue, minimumValue);

		double sum = 0.0;
		try {
			for (int i = 0; i < paretoFrontApproximation.size(); i++)
				sum += Math.pow(QualityIndicatorUtil.distanceToNearestPoint(normalizedApproximation[i], normalizedReference), pow);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}

		sum = Math.pow(sum, 1.0 / pow);

		return sum / normalizedApproximation.length;
	}

	@Override
	public boolean isMin() {
		return true;
	}

	@Override
	public IndicatorType getIndicatorType() {
		return QualityIndicator.IndicatorType.UNARY;
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
