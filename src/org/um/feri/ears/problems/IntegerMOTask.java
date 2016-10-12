package org.um.feri.ears.problems;

import org.um.feri.ears.problems.moo.IntegerMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class IntegerMOTask extends MOTask<Integer, IntegerMOProblem>{
	
	public IntegerMOTask(EnumStopCriteria stop, int eval, double epsilon, IntegerMOProblem p) {
		super(stop, eval, epsilon, p);
	}
	
	public IntegerMOTask(IntegerMOTask task) {
		super(task);
	}

	public MOSolutionBase<Integer> getRandomMOSolution() throws StopCriteriaException {

		if (stopCriteria == EnumStopCriteria.EVALUATIONS) {
			incEvaluate();
			MOSolutionBase<Integer> newSolution = p.getRandomSolution();
			//p.evaluateConstraints(newSolution);
			return newSolution;
		}
		assert false; // Execution should never reach this point!
		return null; //error
	}
	
	public boolean areDimensionsInFeasableInterval(ParetoSolution<Integer> bestByALg) {
	    return p.areDimensionsInFeasableInterval(bestByALg);
	}


	public Integer[] getLowerLimit() {
		Integer[] arr = new Integer[p.lowerLimit.size()];
		arr = p.lowerLimit.toArray(arr);
		return arr;
	}

	public Integer[] getUpperLimit() {
		Integer[] arr = new Integer[p.upperLimit.size()];
		arr = p.upperLimit.toArray(arr);
		return arr;
	}
}
