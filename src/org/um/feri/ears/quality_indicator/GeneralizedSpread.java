//  GeneralizedSpread.java
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

import java.util.Arrays;

/**
 * This class implements the generalized spread metric for two or more dimensions.
 * Reference: A. Zhou, Y. Jin, Q. Zhang, B. Sendhoff, and E. Tsang
 * Combining model-based and genetics-based offspring generation for
 * multi-objective optimization using a convergence criterion,
 * 2006 IEEE Congress on Evolutionary Computation, 2006, pp. 3234-3241.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @author Juan J. Durillo
 */
public class GeneralizedSpread<T extends Number> extends QualityIndicator<T> {

    public GeneralizedSpread(int numObj, String fileName) {
        super(numObj, fileName, (ParetoSolution<T>) getReferenceSet(fileName));
        name = "Generalized Spread";
    }

    @Override
    public double evaluate(ParetoSolution<T> paretoFrontApproximation) {

        /**
         * Stores the normalized approximation set.
         */
        double[][] normalizedApproximation;
        normalizedApproximation = QualityIndicatorUtil.getNormalizedFront(paretoFrontApproximation.writeObjectivesToMatrix(), maximumValue, minimumValue);

        double[][] extremeValues = new double[numberOfObjectives][numberOfObjectives];
        for (int i = 0; i < numberOfObjectives; i++) {
            int finalI = i;
            Arrays.sort(normalizedReference, (o1, o2) -> Double.compare(o2[finalI], o1[finalI]));
            System.arraycopy(normalizedReference[normalizedReference.length - 1], 0, extremeValues[i], 0, numberOfObjectives);
        }

        int numberOfPoints = normalizedApproximation.length;
        int numberOfTruePoints = normalizedReference.length;


        // STEP 4. Sorts the normalized front
        Arrays.sort(normalizedApproximation, new LexicoGraphicalComparator());

        try {
            // STEP 5. Calculate the metric value. The value is 1.0 by default
            if (QualityIndicatorUtil.distance(normalizedApproximation[0], normalizedApproximation[normalizedApproximation.length - 1]) == 0.0) {
                return 1.0;
            } else {

                double dmean = 0.0;

                // STEP 6. Calculate the mean distance between each point and its nearest neighbor
                for (double[] aNormalizedFront : normalizedApproximation) {
                    dmean += QualityIndicatorUtil.distanceToNearestPoint(aNormalizedFront, normalizedApproximation);
                }

                dmean = dmean / (numberOfPoints);

                // STEP 7. Calculate the distance to extremal values
                double dExtrems = 0.0;
                for (double[] extremValue : extremeValues) {
                    dExtrems += QualityIndicatorUtil.distanceToNearestPoint(extremValue, normalizedApproximation);
                }

                // STEP 8. Computing the value of the metric
                double mean = 0.0;
                for (double[] aNormalizedFront : normalizedApproximation) {
                    mean += Math.abs(QualityIndicatorUtil.distanceToNearestPoint(aNormalizedFront, normalizedApproximation) -
                            dmean);
                }

                return (dExtrems + mean) / (dExtrems + (numberOfPoints * dmean));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1.0;
        }
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
