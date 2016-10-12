package org.um.feri.ears.problems;

import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;

public class DoubleMOTask extends MOTask<Double, DoubleMOProblem>{
	
	public DoubleMOTask(EnumStopCriteria stop, int eval, double epsilon, DoubleMOProblem p) {
		super(stop, eval, epsilon, p);
	}
		
	public DoubleMOTask(DoubleMOTask task) {
		super(task);
	}

	public MOSolutionBase<Double> getRandomMOSolution() throws StopCriteriaException {

		if (stopCriteria == EnumStopCriteria.EVALUATIONS) {
			incEvaluate();
			MOSolutionBase<Double> newSolution = p.getRandomSolution();
			//p.evaluateConstraints(newSolution);
			return newSolution;
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
}
