package org.um.feri.ears.qualityIndicator;

import java.util.Comparator;

import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.DominanceComparator;

/**
 * This class implements the RNI (Ratio Of Nondominated Individuals) indicator.
 * 
 * The indicator calculates the ratio of nondominated individuals. The RNI value
 * is in the interval [0,1], the larger is the better.
 * The value RNI = 1 means all the individuals in the population 
 * are non-dominated while the opposite, RNI = 0 represents the 
 * situation where none of the individuals in the population are non-dominated.
 * 
 * Reference: K. C. Tan, T. H. Lee, and E. F. Khor, "Evolutionary algorithms for multiobjective
 * optimization: Performance assessments and comparisons,"
 * Artificial Intell. Rev., vol. 17, no. 4, pp. 253-290, 2002.
 */
public class RatioOfNondominatedIndividuals<T extends Number> extends QualityIndicator<T>{

	public RatioOfNondominatedIndividuals(int num_obj) {
		super(num_obj);
		name = "Ratio Of Nondominated Individuals";
	}
	
	/**
	 * stores a <code>Comparator</code> for dominance checking
	 */
	private static final Comparator<MOSolutionBase> dominance_ = new DominanceComparator();
	
	@Override
	public double evaluate(ParetoSolution<T> population) {
		
		int flagDominate;
		int[] dominateMe = new int[population.size()];
		
		// Set of nondominated individuals
		int X = 0;
		
		for (int p = 0; p < (population.size() - 1); p++) {
			// For all q individuals , calculate if p dominates q or vice versa
			for (int q = p + 1; q < population.size(); q++) {
				// flagDominate
				// =constraint_.compare(solutionSet.get(p),solutionSet.get(q));
				flagDominate = 0;
				if (flagDominate == 0) {
					flagDominate = dominance_.compare(population.get(p),population.get(q));
				}
				if (flagDominate == -1) {
					dominateMe[q]++;
				} else if (flagDominate == 1) {
					dominateMe[p]++;
				}
			}
		}
		// Sum all nondominated individuals
		for (int p = 0; p < population.size(); p++) {
			if (dominateMe[p] == 0) {
				X++;
			}
		}
		double RNI = (double)X / (double)population.size();
		return RNI;
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
