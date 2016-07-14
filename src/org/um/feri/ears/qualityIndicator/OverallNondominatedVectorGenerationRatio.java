package org.um.feri.ears.qualityIndicator;

import java.util.Comparator;

import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.DominanceComparator;

/**
 * This class implements the ONVGR (Overall Nondominated Vector Generation and Ratio) indicator.
 * The indicator measures the ratio of the total number of nondominated vectors 
 * found in PF_known during MOEA execution to the number of vectors found in PF_true.
 * 
 * Reference: D. A. Van Veldhuizen and G. B. Lamont,
 * "On measuring multiobjective evolutionary algorithm performance", 
 * Proc. Congr. Evol. Comput., 2000, pp. 204-211.
 */
public class OverallNondominatedVectorGenerationRatio<T extends Number> extends QualityIndicator<T> {

	public OverallNondominatedVectorGenerationRatio(int num_obj, String file_name) {
		super(num_obj, file_name, (ParetoSolution<T>) getReferenceSet(file_name));
		name = "Overall Nondominated Vector Generation Ratio";
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
		double PF_false = 0;
		double PF_true = 0;
		
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
		for (int p = 0; p < population.size(); p++) {
			if (dominateMe[p] == 0) {
				PF_false++;
			}
		}
		
		dominateMe = new int[referenceSet.length];
		for (int p = 0; p < (referenceSet.length - 1); p++) {
			// For all q individuals , calculate if p dominates q or vice versa
			for (int q = p + 1; q < referenceSet.length; q++) {
				// flagDominate
				// =constraint_.compare(solutionSet.get(p),solutionSet.get(q));
				flagDominate = 0;
				if (flagDominate == 0) {
					flagDominate = dominance_.compare(referencePopulation.get(p),referencePopulation.get(q));
				}
				if (flagDominate == -1) {
					dominateMe[q]++;
				} else if (flagDominate == 1) {
					dominateMe[p]++;
				}
			}
		}
		for (int p = 0; p < referenceSet.length; p++) {
			if (dominateMe[p] == 0) {
				PF_true++;
			}
		}
		
		return PF_false / PF_true;
	}

	@Override
	public boolean isMin() {

		return false;
	}

	@Override
	public IndicatorType getIndicatorType() {
		return QualityIndicator.IndicatorType.Unary;
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
