package org.um.feri.ears.quality_indicator;

import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.DominanceComparator;

import java.util.Comparator;

/**
 * This class implements the ONVG (Overall Nondominated Vector Generation) indicator.
 * The metric measures the number of nondominated individuals found in an
 * approximation front during MOEA evolution. Too few individuals in PF_known make the front�s
 * representation poor and too many vectors may overwhelm
 * the decision maker. It should be noted that if algorithm A
 * outperforms B on this metric does not necessarily imply
 * that algorithm A is clearly better than B.
 * <p>
 * Reference: D. A. Van Veldhuizen, "Multiobjective evolutionary algorithms:
 * Classifications, analyses, and new innovations," Ph.D. dissertation,
 * Air Force Inst. Technol., Wright-Patterson AFB, OH, 1999.
 */
public class OverallNondominatedVectorGeneration<T extends Number> extends QualityIndicator<T> {


    public OverallNondominatedVectorGeneration(int numObj) {
        super(numObj);
        name = "Overall Nondominated Vector Generation";
    }

    /**
     * stores a <code>Comparator</code> for dominance checking
     */
    private static final Comparator<MOSolutionBase> dominance_ = new DominanceComparator();

    @Override
    public double evaluate(ParetoSolution<T> paretoFrontApproximation) {

        int flagDominate;
        int[] dominateMe = new int[paretoFrontApproximation.size()];

        // Set of nondominated individuals
        double PFFalse = 0;

        for (int p = 0; p < (paretoFrontApproximation.size() - 1); p++) {
            // For all q individuals , calculate if p dominates q or vice versa
            for (int q = p + 1; q < paretoFrontApproximation.size(); q++) {
                // flagDominate
                // =constraint_.compare(solutionSet.get(p),solutionSet.get(q));
                flagDominate = dominance_.compare(paretoFrontApproximation.get(p), paretoFrontApproximation.get(q));
                if (flagDominate == -1) {
                    dominateMe[q]++;
                } else if (flagDominate == 1) {
                    dominateMe[p]++;
                }
            }
        }
        for (int p = 0; p < paretoFrontApproximation.size(); p++) {
            if (dominateMe[p] == 0) {
                PFFalse++;
            }
        }
        return PFFalse;
    }

    @Override
    public boolean isMin() {
        return false;
    }

    @Override
    public IndicatorType getIndicatorType() {
        return QualityIndicator.IndicatorType.UNARY;
    }

    @Override
    public boolean requiresReferenceSet() {
        return false;
    }

    @Override
    public int compare(ParetoSolution<T> front1, ParetoSolution<T> front2, Double epsilon) {
        return 0;
    }

}
