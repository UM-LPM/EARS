//  InvertedGenerationalDistance.java
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

package org.um.feri.ears.quality_indicator;

import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.EuclideanDistance;

/**
 * This class implements the inverted generational distance metric.
 * It can be used also as a command line by typing:
 * "java jmetal.qualityIndicator.InvertedGenerationalDistance <solutionFrontFile> <trueFrontFile>
 * <numberOfObjectives>"
 * Reference: P. A. N.~Bosman and D.~Thierens. The balance between proximity and
 * diversity in multiobjective evolutionary algorithms. IEEE Trans. on
 * Evolutionary Computation, 7(2):174--188, 2003.
 */
public class InvertedGenerationalDistance<T extends Number> extends QualityIndicator<T> {
    static final double pow = 2.0; // pow. This is the pow used for the distances

    /**
     * Constructor. Creates a new instance of the generational distance metric.
     */
    public InvertedGenerationalDistance(int numObj, String problemName) {
        super(numObj, problemName, getReferenceSet(problemName));
        name = "Inverted Generational Distance";
    }

    public InvertedGenerationalDistance(int numObj, String problemName, double[][] referenceFront, double[] referencePoint) {
        super(numObj, problemName, referenceFront, referencePoint);
        name = "Inverted Generational Distance";
    }

    @Override
    public double evaluate(double[][] paretoFrontApproximation) {

        double[][] normalizedApproximation;

        normalizedApproximation = QualityIndicatorUtil.normalizeFront(paretoFrontApproximation, maximumValue, minimumValue);

        // Sum the distances between each point of the true Pareto front
        // and the nearest point in the true Pareto front
        double sum = 0.0;
        try {
            for (double[] referencePoint : normalizedReference)
                sum += Math.pow(QualityIndicatorUtil.distanceToNearestPoint(referencePoint, normalizedApproximation, new EuclideanDistance()), pow);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Obtain the sqrt of the sum
        sum = Math.pow(sum, 1.0 / pow);

        // Divide the sum by the maximum number of points of the front
		return sum / normalizedReference.length;
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
