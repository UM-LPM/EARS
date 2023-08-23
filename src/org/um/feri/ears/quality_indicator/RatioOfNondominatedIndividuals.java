package org.um.feri.ears.quality_indicator;

import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.DominanceComparator;
import org.um.feri.ears.util.comparator.SolutionDominanceComparator;

/**
 * This class implements the RNI (Ratio Of Nondominated Individuals) indicator.
 * <p>
 * The indicator calculates the ratio of nondominated individuals. The RNI value
 * is in the interval [0,1], the larger is the better.
 * The value RNI = 1 means all the individuals in the population
 * are non-dominated while the opposite, RNI = 0 represents the
 * situation where none of the individuals in the population are non-dominated.
 * <p>
 * Reference: K. C. Tan, T. H. Lee, and E. F. Khor, "Evolutionary algorithms for multiobjective
 * optimization: Performance assessments and comparisons,"
 * Artificial Intell. Rev., vol. 17, no. 4, pp. 253-290, 2002.
 */
public class RatioOfNondominatedIndividuals<T extends Number> extends QualityIndicator<T> {

    public RatioOfNondominatedIndividuals(int num_obj) {
        super(num_obj);
        name = "Ratio Of Nondominated Individuals";
    }

    /**
     * stores a <code>Comparator</code> for dominance checking
     */
    private static final DominanceComparator dominance = new DominanceComparator();

    @Override
    public double evaluate(double[][] paretoFrontApproximation) {

        int flagDominate;
        int[] dominateMe = new int[paretoFrontApproximation.length];

        // Set of nondominated individuals
        int X = 0;

        for (int p = 0; p < (paretoFrontApproximation.length - 1); p++) {
            // For all q individuals , calculate if p dominates q or vice versa
            for (int q = p + 1; q < paretoFrontApproximation.length; q++) {
                // flagDominate
                // =constraint_.compare(solutionSet.get(p),solutionSet.get(q));
				flagDominate = dominance.compare(paretoFrontApproximation[p], paretoFrontApproximation[q]);
				if (flagDominate == -1) {
                    dominateMe[q]++;
                } else if (flagDominate == 1) {
                    dominateMe[p]++;
                }
            }
        }
        // Sum all nondominated individuals
        for (int p = 0; p < paretoFrontApproximation.length; p++) {
            if (dominateMe[p] == 0) {
                X++;
            }
        }
		return (double) X / (double) paretoFrontApproximation.length;
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
    public int compare(ParetoSolution front1, ParetoSolution front2, Double epsilon) {
        return 0;
    }

}
