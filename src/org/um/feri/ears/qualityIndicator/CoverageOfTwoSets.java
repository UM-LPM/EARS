package org.um.feri.ears.qualityIndicator;

import java.util.Comparator;

import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.DominanceComparator;

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
 * 
 * Reference: E. Zitzler, K. Deb, and L. Thiele. Comparison of
 * multiobjective evolutionary algorithms: Empirical
 * results. Journal of Evolutionary Computation, 8(2),
 * 2000,pp.173-195.
 */
public class CoverageOfTwoSets<Type> extends QualityIndicator<Type> {
	
	public CoverageOfTwoSets(MOProblemBase moProblemBase) {
		super(moProblemBase);
		name = "Coverage of two sets";
	}

	/**
	 * stores a <code>Comparator</code> for dominance checking
	 */
	private static final Comparator<MOSolutionBase> dominance_ = new DominanceComparator();
	
	@Override
	public double evaluate(ParetoSolution front) {
		// TODO calculate throw error if second front is null
		double CS = 0.0;
		
		return CS;
	}

	@Override
	public IndicatorType getIndicatorType() {
		return IndicatorType.Binary;
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
	public int compare(ParetoSolution front1, ParetoSolution front2, Double epsilon) {
		double CS1 = 0.0;
		double CS2 = 0.0;
		
		int flagDominate;
		boolean[] dominateFront1 = new boolean[front1.size()];
		boolean[] dominateFront2 = new boolean[front2.size()];
		
		for (int p = 0; p < (front1.size() - 1); p++) {
			// For all q individuals , calculate if p dominates q or vice versa
			for (int q = 0; q < front2.size(); q++) {
				// flagDominate
				flagDominate = 0;
				if (flagDominate == 0) {
					flagDominate = dominance_.compare(front1.get(p),front2.get(q));
				}
				if (flagDominate == -1) {
					dominateFront2[q] = true;
				} else if (flagDominate == 1) {
					dominateFront1[p] = true;
				}
			}
		}
		
		//the number of dominate solutions in front2 by front1
		for (boolean f2 : dominateFront2) {
			if(f2)
				CS1++;
		}
		//the number of dominate solutions in front1 by front2
		for (boolean f1 : dominateFront1) {
			if(f1)
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
		
		if((CS1 - CS2) < epsilon)
			return 0;
		
		if(CS1 > CS2)
			return -1;
		if(CS1 < CS2)
			return 1;
		
		return 0;
	}

}
