package org.um.feri.ears.qualityIndicator;

import java.util.Comparator;

import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.Comparator.DominanceComparator;

/**
 * This class implements the ONVG (Overall Nondominated Vector Generation) indicator.
 * The metric measures the number of nondominated individuals found in an
 * approximation front during MOEA evolution. Too few individuals in PF_known make the front�s
 * representation poor and too many vectors may overwhelm
 * the decision maker. It should be noted that if algorithm A
 * outperforms B on this metric does not necessarily imply
 * that algorithm A is clearly better than B.
 * 
 * Reference: D. A. Van Veldhuizen, "Multiobjective evolutionary algorithms: 
 * Classifications, analyses, and new innovations," Ph.D. dissertation, 
 * Air Force Inst. Technol., Wright-Patterson AFB, OH, 1999.
 */
public class OverallNondominatedVectorGeneration<T extends Number> extends QualityIndicator<T> {

	
	public OverallNondominatedVectorGeneration(int num_obj) {
		super(num_obj);
		name = "Overall Nondominated Vector Generation";
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
		
		return PF_false;
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
