package org.um.feri.ears.problems;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.EnumAlgorithmParameters;

public abstract class TaskBase<T extends ProblemBase> {
	
	protected EnumStopCriterion stopCriterion;
	protected int maxEvaluations; // for Stop criterion
	protected int numberOfEvaluations = 0; // for Stop criterion
	protected double epsilonForGlobal; // epsilon representing the error margin difference when comparing a solution to the global optimum
	protected boolean isStop;
	protected boolean isGlobal;
	protected int precisionOfRealNumbersInDecimalPlaces; //used only for discreet problem presentation (bit presentation in GA)
	protected T p;
	private int resetCount;
	
	protected long evaluationTime = 0;
	protected long timerStart;
	protected long allowedCPUTime; // nanoseconds
	protected int numberOfIterations = 0;
	protected int maxIterations;
    protected int maxTrialsBeforeStagnation = 10000;
    protected int stagnationTrialCounter = 0;
	/**
	 * Keeps track of the best solution found.
	 */
	protected double bestEval;
	
	//protected StringBuilder ancestorSB;
	protected ArrayList<DoubleSolution> ancestors;
	protected boolean isAncestorLoggingEnabled = false;
	protected ArrayList<EvaluationStorage.Evaluation> evaluationHistory;
	protected boolean isEvaluationHistoryEnabled = false;

	/**
	 * Has the global optimum been reached.
	 * @return true if the global optimum found else false.
	 */
	public boolean isGlobal() {
		return isGlobal;
	}

	public long getEvaluationTimeNs() {
		return evaluationTime;
	}
	
	public long getEvaluationTimeMs() {
		return TimeUnit.NANOSECONDS.toMillis(evaluationTime);
	}

	public EnumStopCriterion getStopCriterion() {
		return stopCriterion;
	}

	public TaskBase() {
		resetCount = 0;
	}
	
	public void startTimer()
	{
		timerStart = System.nanoTime();
	}

	/**
	 * Increments the number of iterations.
	 * @throws StopCriterionException if the number of iterations exceeds the maximum number of iterations.
	 */
	public void incrementNumberOfIterations() throws StopCriterionException
	{
		if(stopCriterion == EnumStopCriterion.ITERATIONS)
		{
			if (numberOfIterations >= maxIterations)
				throw new StopCriterionException("Max iterations");
		}
		
		numberOfIterations++;
		if (numberOfIterations >= maxIterations && stopCriterion == EnumStopCriterion.ITERATIONS)
		{
			isStop = true;
		}
	}

	/**
	 * Increments the number of evaluations. This method shouldn't be called manually.
	 * @throws StopCriterionException if the number of evaluations exceeds the maximum number of evaluations .
	 */
	public void incrementNumberOfEvaluations() throws StopCriterionException {
		if (numberOfEvaluations >= maxEvaluations && stopCriterion == EnumStopCriterion.EVALUATIONS)
			throw new StopCriterionException("Max evaluations");
		numberOfEvaluations++;
		if (numberOfEvaluations >= maxEvaluations && (stopCriterion == EnumStopCriterion.EVALUATIONS || stopCriterion == EnumStopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS))
			isStop = true;
	}

	/**
     * When comparing a solution to the global optimum and the error margin difference is less or equal to epsilon, then the
     * global optimum has been reached
     * 
     * @return  epsilon representing the error margin difference when comparing a solution to the global optimum
     */
	public double getEpsilonForGlobal() {
	    return epsilonForGlobal;
	}
	
	public int getNumberOfDimensions() {
		return p.getNumberOfDimensions();
	}
	
	public int getNumberOfConstrains() {
	    return p.numberOfConstraints;
	}
	
	public void enableAncestorLogging()
	{
		isAncestorLoggingEnabled = true;
		if(ancestors == null)
		ancestors = new ArrayList<DoubleSolution>();
	}

	public void disableAncestorLogging()
	{
		isAncestorLoggingEnabled = false;
	}

	public void enableEvaluationHistory()
	{
		isEvaluationHistoryEnabled = true;
		if(evaluationHistory == null)
			evaluationHistory = new ArrayList<>();
	}

	public void disableEvaluationHistory()
	{
		isEvaluationHistoryEnabled = false;
	}

	public ArrayList<EvaluationStorage.Evaluation> getEvaluationHistory() {
		return evaluationHistory;
	}
	public ArrayList<DoubleSolution> getAncestors() {
		return ancestors;
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
	
	public int getMaxIterations() {
		return maxIterations;
	}
	
	public long getAllowedCPUTime() {
		return allowedCPUTime;
	}
	
	public int getNumberOfEvaluations(){
		return numberOfEvaluations;
	}
	
	public int getNumberOfIterations() {
		return numberOfIterations;
	}
	
	public long getAvailableCPUTime() {
		return allowedCPUTime - System.nanoTime();
	}
	
	public int getMaxTrialsBeforeStagnation() {
		return maxTrialsBeforeStagnation;
	}
	
	public long getUsedCPUTime() {
		return System.nanoTime() - timerStart;
	}
	
	public boolean isStopCriterion() {
		
		if(stopCriterion == EnumStopCriterion.CPU_TIME)
		{
			hasTheCPUTimeBeenExceeded();
		}

		//stop only if stop criteria set to global optimum
		if (stopCriterion == EnumStopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS && isGlobal)
			return true;

		return isStop;
	}
	
	public boolean hasTheCPUTimeBeenExceeded()
	{
		if(numberOfEvaluations == 0){ //set start time if 0 evaluations
			this.startTimer();
		}
		
		if(System.nanoTime() - timerStart > allowedCPUTime){
			isStop = true;
			return true;
		}
		return false;
	}
	
	public String getStopCriterionDescription() {
        if (stopCriterion == EnumStopCriterion.EVALUATIONS) {
            return "E="+getMaxEvaluations();
        }
        if (stopCriterion == EnumStopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
                return "Global optimum epsilon="+ epsilonForGlobal +" or  E="+getMaxEvaluations();
        }
        if(stopCriterion == EnumStopCriterion.ITERATIONS)
        {
        	return "ITERATIONS="+ getMaxIterations();
        }
        if(stopCriterion == EnumStopCriterion.CPU_TIME)
        {
        	return "TIME="+TimeUnit.NANOSECONDS.toMillis(getAllowedCPUTime())+" ms";
        }
        if(stopCriterion == EnumStopCriterion.STAGNATION)
        {
        	return "STAGNATION= trials: "+ stagnationTrialCounter;
        }
        return "not defined";
	}

	/**
	 * Use to check if the problem is to be minimized.
	 * 
	 * @return true if the problem is to be minimized
	 */
	public boolean isMinimize() {
	    return p.isMinimize();
	}
	
    /**
     * @return name of the problem
     */
    public String getProblemName() {
        return p.getName();
    }

	public String getFileNameString() {
    	return p.getFileNameString();
	}
    
    public int getProblemHashCode()
    {
    	return p.hashCode();
    }
    
    public void resetCounter() {
    	resetCount++;
        numberOfEvaluations = 0;
        numberOfIterations = 0;
        evaluationTime = 0;
        isStop = false;
        isGlobal = false;
        timerStart = System.nanoTime();
        stagnationTrialCounter = 0;
    }
    
    public int getResetCount()
    {
    	return resetCount;
    }

    @Override
    public String toString() {
        return "Task [stopCriterion=" + stopCriterion + ", maxEvaluations=" + maxEvaluations + ", numberOfEvaluations=" + numberOfEvaluations + ", epsilon="
                + epsilonForGlobal + ", isStop=" + isStop + ", isGlobal=" + isGlobal + ", precisionOfRealNumbersInDecimalPlaces="
                + precisionOfRealNumbersInDecimalPlaces + ", p=" + p + "]";
    }
    
    public String getTaskInfoCSV(){
        return  p.getProblemInfoCSV() + "task stop criterion:" + stopCriterion + ",maxEvaluations:" + maxEvaluations + ",epsilon:"+ epsilonForGlobal + ",";
    }
    
    /**
     * Returns a string containing all the tasks information that doesn't change.
     * @return string containing all information about the task
     */
    public String getTaskInfo() {

    	if(stopCriterion == EnumStopCriterion.EVALUATIONS) {
    		return "Task = " + p +" stopCriterion=" + stopCriterion + ", maxEvaluations=" + maxEvaluations + ", epsilon="
    				+ epsilonForGlobal + ", precisionOfRealNumbersInDecimalPlaces="
    				+ precisionOfRealNumbersInDecimalPlaces;
    	}
    	else if(stopCriterion == EnumStopCriterion.ITERATIONS) {
    		return "Task = " + p +" stopCriterion=" + stopCriterion + ", maxIterations=" + maxIterations + ", epsilon="
    				+ epsilonForGlobal + ", precisionOfRealNumbersInDecimalPlaces="
    				+ precisionOfRealNumbersInDecimalPlaces;
    	}
    	else if(stopCriterion == EnumStopCriterion.CPU_TIME){
    		return "Task = " + p +" stopCriterion=" + stopCriterion + ", allowedCPUTime=" + allowedCPUTime + ", epsilon="
    				+ epsilonForGlobal + ", precisionOfRealNumbersInDecimalPlaces="
    				+ precisionOfRealNumbersInDecimalPlaces;
    	}
    	else
    		return "";
    }
}
