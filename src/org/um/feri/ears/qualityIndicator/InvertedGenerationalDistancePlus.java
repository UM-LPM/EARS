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
public class InvertedGenerationalDistancePlus<T extends Number> extends QualityIndicator<T> {


    public InvertedGenerationalDistancePlus(int num_obj, String file_name) {
        super(num_obj, file_name, (ParetoSolution<T>) getReferenceSet(file_name));
        name = "Inverted Generational Distance Plus";
    }

    @Override
    public double evaluate(ParetoSolution<T> paretoFrontApproximation) {


        double[][] normalizedApproximation;
        normalizedApproximation = QualityIndicatorUtil.getNormalizedFront(paretoFrontApproximation.writeObjectivesToMatrix(), maximumValue, minimumValue);

        double sum = 0.0;
        try {
            for (int i = 0; i < normalizedReference.length; i++) {
                sum += QualityIndicatorUtil.distanceToNearestPoint(normalizedReference[i], normalizedApproximation, new DominanceDistance());
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
