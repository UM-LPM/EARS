package org.um.feri.ears.quality_indicator;

import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.comparator.SolutionDominanceComparator;

/**
 * This class implements the Coverage of two sets indicator.
 * CS (Coverage of two Sets) is used to compare
 * two sets of non-dominated solutions. The function CS
 * maps the ordered pair (A,B) into the interval[0,1].
 * The value CS(A,B) = 1 means that all solutions in
 * B are dominated by A and the value
 * CS(A,B) = 0 indicates that none of the solutions in
 * B are dominated by A . Note that both  CS(A,B) and  CS(B,A) have
 * to be considered since CS(B,A) is not necessary
 * equal to 1 - CS(A,B).
 * <p>
 * Reference: E. Zitzler, K. Deb, and L. Thiele. Comparison of
 * multiobjective evolutionary algorithms: Empirical
 * results. Journal of Evolutionary Computation, 8(2),
 * 2000,pp.173-195.
 */
public class CoverageOfTwoSets<N extends Number> extends QualityIndicator<N> {

    public CoverageOfTwoSets(int numObj) {
        super(numObj);
        name = "Coverage of two sets";
    }

    /**
     * stores a <code>Comparator</code> for dominance checking
     */
    private final SolutionDominanceComparator dominance = new SolutionDominanceComparator();

    @Override
    public double evaluate(double[][] paretoFrontApproximation) {
        // TODO calculate throw error if second front is null
        System.err.println("Cannot evaluate with binary indicator!");
        return Double.MAX_VALUE;
    }

    @Override
    public IndicatorType getIndicatorType() {
        return IndicatorType.BINARY;
    }

    @Override
    public boolean isMin() {
        return false;
    }

    @Override
    public boolean requiresReferenceSet() {
        return true;
    }

    @Override
    public int compare(ParetoSolution<N> front1, ParetoSolution<N> front2, Double epsilon) {
        double CS1 = 0.0;
        double CS2 = 0.0;

        int flagDominate;
        boolean[] dominateFront1 = new boolean[front1.size()];
        boolean[] dominateFront2 = new boolean[front2.size()];

        for (int p = 0; p < (front1.size() - 1); p++) {
            // For all q individuals , calculate if p dominates q or vice versa
            for (int q = 0; q < front2.size(); q++) {
                // flagDominate
                flagDominate = dominance.compare(front1.get(p), front2.get(q));
                if (flagDominate == -1) {
                    dominateFront2[q] = true;
                } else if (flagDominate == 1) {
                    dominateFront1[p] = true;
                }
            }
        }

        //the number of dominated solutions in front2 by front1
        for (boolean f2 : dominateFront2) {
            if (f2)
                CS1++;
        }
        //the number of dominated solutions in front1 by front2
        for (boolean f1 : dominateFront1) {
            if (f1)
                CS2++;
        }


        CS1 = CS1 / front2.size();
        CS2 = CS2 / front1.size();

        //table 5 E. Zitzler, Performance assessment of multiobjective optimizers: An analysis and review
		/*
		if(CS1 == CS2)
			return 0;
		if((0 < CS1 && CS1 < 1) && (0 < CS2 && CS2 < 1))
			return 0;
		
		if(CS1 == 1 && CS2 < 1)
			return -1;
		if(CS2 == 1 && CS1 < 1)
			return 1;
		*/

        if (Math.abs(CS1 - CS2) < epsilon)
            return 0;

        return Double.compare(CS2, CS1);

    }

}
