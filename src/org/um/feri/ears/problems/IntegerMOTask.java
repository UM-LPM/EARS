package org.um.feri.ears.problems;

import org.um.feri.ears.problems.moo.IntegerMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class IntegerMOTask extends MOTask<Integer, IntegerMOProblem>{
	
	public IntegerMOTask(EnumStopCriteria stop, int eval, long allowedTime, int maxIterations, double epsilon, IntegerMOProblem p) {
		super(stop, eval, allowedTime, maxIterations, epsilon, p);
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
		else if(stopCriteria == EnumStopCriteria.ITERATIONS)
		{
			if(isStop)
				throw new StopCriteriaException("Max iterations");
			
			incEvaluate();
			MOSolutionBase<Integer> newSolution = p.getRandomSolution();
			return newSolution;
			
		}
		else if(stopCriteria == EnumStopCriteria.CPU_TIME)
		{
			// check if the CPU time is not exceeded yet
			if(!isStop)
			{
				hasTheCPUTimeBeenExceeded(); // if CPU time is exceed allow last eval
				incEvaluate();
				MOSolutionBase<Integer> newSolution = p.getRandomSolution();
				return newSolution;
			}
			else
			{
				throw new StopCriteriaException("CPU Time");
			}
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

	@Override
	public MOTask returnCopy() {
		return new IntegerMOTask(this);
	}
}
