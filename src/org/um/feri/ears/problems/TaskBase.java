package org.um.feri.ears.problems;

import org.um.feri.ears.benchmark.MORatingBenchmark;
import org.um.feri.ears.benchmark.RatingBenchmarkBase;

public abstract class TaskBase<T extends ProblemBase> {
	
	protected EnumStopCriteria stopCriteria;
	protected int maxEvaluations; // for Stop criteria
	protected int numberOfEvaluations = 0; // for Stop criteria
	protected double epsilon; // for Stop criteria
	protected boolean isStop;
	protected boolean isGlobal;
	protected int precisionOfRealNumbersInDecimalPlaces; //used only for discreet problem presentation (bit presentation in GA)
	protected T p;
	private int resetCount;
	
	public TaskBase() {
		resetCount = 0;
	}

	/**
     * When you subtract 2 solutions and difference is less or equal epsilon,
     * solution are treated as equal good (draw in algorithm match)!
     * 
     * @return condition that is used when 2 solutions are equal good!
     */
	public double getEpsilon() {
	    return epsilon;
	}
	
	public int getDimensions() {
		return p.getNumberOfDimensions();
	}
	
	public int getNumberOfConstrains() {
	    return p.numberOfConstraints;
	}
	
	/**
	 * Used only for discreet problem presentation (bit presentation in GA)
	 * @return
	 */
	public int getPrecisionMinDecimal() {
	    return precisionOfRealNumbersInDecimalPlaces;
	}
	
	public int getMaxEvaluations() {
        return maxEvaluations;
    }
	
	public int getNumberOfEvaluations(){
		return numberOfEvaluations;
	}
	
	public boolean isStopCriteria() {
		return isStop||isGlobal;
	}
	
	public String getStopCriteriaDescription() {
        if (stopCriteria == EnumStopCriteria.EVALUATIONS) {
            return "E="+getMaxEvaluations();
        }
        if (stopCriteria == EnumStopCriteria.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
                return "Global optimum epsilon="+epsilon+" or  E="+getMaxEvaluations();
        }
        return "not defened";
	}
	
	protected void incEvaluate() throws StopCriteriaException {
		if (numberOfEvaluations >= maxEvaluations)
			throw new StopCriteriaException("Max evaluations");
		numberOfEvaluations++;
		if (numberOfEvaluations >= maxEvaluations)
			isStop = true;
	}
	
	/**
	 * @deprecated
	 * Deprecated is because it is better to use individuals and
	 * isFirstBetter that already is influenced by this parameter.
	 * Returns true id global maximum searching!
	 * 
	 * @return
	 */
	public boolean isMaximize() {
	    return !p.minimum;
	}
	
    /**
     * @return
     */
    public String getProblemName() {
        return p.getName();
    }
    
    public void resetCounter() {
    	resetCount++;
        numberOfEvaluations = 0;
        isStop = false;
        isGlobal = false;
    }
    
    public int getResetCount()
    {
    	return resetCount;
    }

}
