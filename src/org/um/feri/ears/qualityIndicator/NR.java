package org.um.feri.ears.qualityIndicator;

import org.um.feri.ears.algorithms.moo.dbea.NondominatedPopulation;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.util.SolutionComparator;

/**
 * This class implements the Pareto dominance indicator.
 * NR measures the ratio of nondominated solutions that 
 * is contributed by a particular solution set to the 
 * nondominated solutions provided

 * Reference: C. K. Goh and K. C. Tan. A competitive-cooperative coevolutionary
 * paradigm for dynamic multiobjective optimization. IEEE Transactions
 * on Evolutionary Computation, 13(1), 103–-127, 2009.
 */
public class NR<T extends Number> extends QualityIndicator<T>{
	
	public NR(int num_obj) {
		super(num_obj);
		name = "NR";
	}

	@Override
	public double evaluate(ParetoSolution<T> population) {
		return 0;
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
		return false;
	}

	@Override
	public int compare(ParetoSolution<T> front1, ParetoSolution<T> front2, Double epsilon) {

		double NR1 = 0.0, NR2 = 0.0;
		
		NondominatedPopulation<T> ndp = new NondominatedPopulation<T>();
		ndp.addAll(front1);
		ndp.addAll(front2);
		
		SolutionComparator<T> sc = new SolutionComparator<T>();
		
		for (MOSolutionBase<T> s1 : front1) {
			for (MOSolutionBase<T> ndSolution : ndp) {
				if(sc.compare(s1, ndSolution) == 0)
					NR1++;
			}
		}
		
		for (MOSolutionBase<T> s2 : front2) {
			for (MOSolutionBase<T> ndSolution : ndp) {
				if(sc.compare(s2, ndSolution) == 0)
					NR2++;
			}
		}
		
		NR1 /= ndp.size();
		NR2 /= ndp.size();
		
		if(NR1 > NR2)
			return -1;
		if(NR1 < NR2)
			return 1;
		
		return 0;
	}

}
