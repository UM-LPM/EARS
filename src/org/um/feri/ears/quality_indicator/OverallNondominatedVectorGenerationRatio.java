package org.um.feri.ears.quality_indicator;

import org.um.feri.ears.problems.NumberSolution;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.DominanceComparator;

import java.util.Comparator;

/**
 * This class implements the ONVGR (Overall Nondominated Vector Generation and Ratio) indicator.
 * The indicator measures the ratio of the total number of nondominated vectors
 * found in PF_known during MOEA execution to the number of vectors found in PF_true.
 * <p>
 * Reference: D. A. Van Veldhuizen and G. B. Lamont,
 * "On measuring multiobjective evolutionary algorithm performance",
 * Proc. Congr. Evol. Comput., 2000, pp. 204-211.
 */
public class OverallNondominatedVectorGenerationRatio<T extends Number> extends QualityIndicator<T> {

    public OverallNondominatedVectorGenerationRatio(int num_obj, String file_name) {
        super(num_obj, file_name, getReferenceSet(file_name));
        name = "Overall Nondominated Vector Generation Ratio";
    }

    /**
     * stores a <code>Comparator</code> for dominance checking
     */
    private final DominanceComparator dominance = new DominanceComparator();

    @Override
    public double evaluate(ParetoSolution<T> paretoFrontApproximation) {

        int flagDominate;
        int[] dominateMe = new int[paretoFrontApproximation.size()];

        // Set of nondominated individuals
        double PFFalse = 0;
        double PFTrue = 0;

        for (int p = 0; p < (paretoFrontApproximation.size() - 1); p++) {
            // For all q individuals , calculate if p dominates q or vice versa
            for (int q = p + 1; q < paretoFrontApproximation.size(); q++) {
                // flagDominate
                // =constraint_.compare(solutionSet.get(p),solutionSet.get(q));
                flagDominate = dominance.compare(paretoFrontApproximation.get(p), paretoFrontApproximation.get(q));
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

        dominateMe = new int[referenceSet.length];
        for (int p = 0; p < (referenceSet.length - 1); p++) {
            // For all q individuals , calculate if p dominates q or vice versa
            for (int q = p + 1; q < referenceSet.length; q++) {
                // flagDominate
                // =constraint_.compare(solutionSet.get(p),solutionSet.get(q));
                flagDominate = dominance.compare(referencePopulation.get(p), referencePopulation.get(q));
                if (flagDominate == -1) {
                    dominateMe[q]++;
                } else if (flagDominate == 1) {
                    dominateMe[p]++;
                }
            }
        }
        for (int p = 0; p < referenceSet.length; p++) {
            if (dominateMe[p] == 0) {
                PFTrue++;
            }
        }
        return PFFalse / PFTrue;
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
        return true;
    }

    @Override
    public int compare(ParetoSolution<T> front1, ParetoSolution<T> front2, Double epsilon) {
        return 0;
    }

}
