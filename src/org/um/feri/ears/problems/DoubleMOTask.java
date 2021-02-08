package org.um.feri.ears.problems;

import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class DoubleMOTask extends MOTask<Double, DoubleMOProblem>{
	
	public DoubleMOTask(EnumStopCriterion stop, int eval, long allowedTime, int maxIterations, double epsilon, DoubleMOProblem p) {
		super(stop, eval, allowedTime, maxIterations, epsilon, p);
	}
		
	public DoubleMOTask(DoubleMOTask task) {
		super(task);
	}

	public MOSolutionBase<Double> getRandomMOSolution() throws StopCriterionException {

		
		if (stopCriterion == EnumStopCriterion.EVALUATIONS) {
			incrementNumberOfEvaluations();
			MOSolutionBase<Double> newSolution = p.getRandomSolution();
			//p.evaluateConstraints(newSolution);
			return newSolution;
		}
		else if(stopCriterion == EnumStopCriterion.ITERATIONS)
		{
			if(isStop)
				throw new StopCriterionException("Max iterations");
			incrementNumberOfEvaluations();
			MOSolutionBase<Double> newSolution = p.getRandomSolution();
			return newSolution;
			
		}
		else if(stopCriterion == EnumStopCriterion.CPU_TIME)
		{
			// check if the CPU time is not exceeded yet
			if(!isStop)
			{
				hasTheCPUTimeBeenExceeded(); // if CPU time is exceed allow last eval
				incrementNumberOfEvaluations();
				MOSolutionBase<Double> newSolution = p.getRandomSolution();
				return newSolution;
			}
			else
			{
				throw new StopCriterionException("CPU Time");
			}
		}
		
		assert false; // Execution should never reach this point!
		return null; //error
	}
	
	 /**
     * with no evaluations just checks
     * if algorithm result is in interval.
     * This is not checking constrains, just basic intervals!  
     * Delegated from Problem!
     * 
     * @param ds vector of possible solution
     * @return
     */
	public boolean areDimensionsInFeasableInterval(ParetoSolution<Double> bestByALg) {
	    return p.areDimensionsInFeasableInterval(bestByALg);
	}

	public Double[] getLowerLimit() {
		Double[] arr = new Double[p.lowerLimit.size()];
		arr = p.lowerLimit.toArray(arr);
		return arr;
	}

	public Double[] getUpperLimit() {
		Double[] arr = new Double[p.upperLimit.size()];
		arr = p.upperLimit.toArray(arr);
		return arr;
	}

	public double getLowerLimit(int i) {
		return p.lowerLimit.get(i);
	}

	public double getUpperLimit(int i) {
		return p.upperLimit.get(i);
	}

	@Override
	public MOTask returnCopy() {
		return new DoubleMOTask(this);
	}
}
