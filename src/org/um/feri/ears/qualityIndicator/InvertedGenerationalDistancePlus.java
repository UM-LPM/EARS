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
import org.um.feri.ears.util.DominanceDistance;

/**
 * This class implements the inverted generational distance metric plust (IGD+)
 * Reference: Ishibuchi et al 2015, "A Study on Performance Evaluation Ability of a Modified
 * Inverted Generational Distance Indicator", GECCO 2015
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class InvertedGenerationalDistancePlus<T extends Number> extends QualityIndicator<T>{


	public InvertedGenerationalDistancePlus(int num_obj, String file_name) {
		super(num_obj, file_name, (ParetoSolution<T>) getReferenceSet(file_name));
		name = "Inverted Generational Distance Plus";
	}
	
	@Override
	public double evaluate(ParetoSolution<T> population) {
		

		double[][] normalizedApproximation;
		normalizedApproximation = MetricsUtil.getNormalizedFront(population.writeObjectivesToMatrix(), maximumValue, minimumValue);
		
	    double sum = 0.0;
	    try {
			for (int i = 0 ; i < normalizedReference.length; i++) {
					sum += MetricsUtil.distanceToNearestPoint(normalizedReference[i], normalizedApproximation, new DominanceDistance());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return sum / normalizedReference.length;
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
