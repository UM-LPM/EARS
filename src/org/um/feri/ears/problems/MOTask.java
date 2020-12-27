package org.um.feri.ears.problems;

import java.util.concurrent.TimeUnit;

import org.um.feri.ears.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.qualityIndicator.QualityIndicator;

public abstract class MOTask<T extends Number , P extends MOProblemBase<T>> extends TaskBase<P>{
	
    /**
     * Task constructor for multiobjective optimization.
     * @param stop
     * @param eval
     * @param epsilon
     * @param p
     * @param qi
     */
    public MOTask(EnumStopCriterion stop, int eval, long allowedTime, int maxIterations, double epsilon, P p) {
    	
    	this(stop, eval, allowedTime, maxIterations, epsilon, p,  (int) Math.log10((1./epsilon)+1));
	}
    
    abstract public boolean areDimensionsInFeasableInterval(ParetoSolution<T> bestByALg);

    /**
     * Task constructor for multiobjective optimization.
     * @param stop
     * @param eval
     * @param epsilon
     * @param p
     * @param qi
     * @param precisonOfRealNumbers
     */
	public MOTask(EnumStopCriterion stop, int eval, long allowedTime, int maxIterations, double epsilon, P p, int precisonOfRealNumbers) {
		
		precisionOfRealNumbersInDecimalPlaces = precisonOfRealNumbers;
        stopCriterion = stop;
        maxEvaluations = eval;
        numberOfEvaluations = 0;
        this.epsilon = epsilon;
        isStop = false;
        isGlobal = false;
        super.p = p; // TODO generic type in TaskBase
        this.allowedCPUTime = TimeUnit.MILLISECONDS.toNanos(allowedTime);
        this.maxIterations = maxIterations;
	}
	
	public MOTask(MOTask<T,P> task)
	{
		precisionOfRealNumbersInDecimalPlaces = task.precisionOfRealNumbersInDecimalPlaces;
        stopCriterion = task.stopCriterion;
        maxEvaluations = task.maxEvaluations;
        numberOfEvaluations = task.numberOfEvaluations;
        epsilon = task.epsilon;
        isStop = task.isStop;
        isGlobal = task.isGlobal;
        maxIterations = task.maxIterations;
        allowedCPUTime = task.allowedCPUTime;
        super.p = task.p;  //TODO deep copy?
	}
	
	/**
	 * @return The number of objectives
	 */
	public int getNumberOfObjectives() {
		return p.getNumberOfObjectives();
	}
	
	/**
	 * @return The file name of the problem
	 */
	public String getProblemFileName() {
		return p.getFileName();
	}
	
	public String getBenchmarkName()
	{
		return p.getBenchmarkName();
	}
	
	public int getNumberOfConstrains() {
	    return p.getNumberOfConstraints();
	}
	
	abstract public MOSolutionBase<T> getRandomMOSolution() throws StopCriterionException;
	
	public boolean isFirstBetter(ParetoSolution<T> x, ParetoSolution<T> y, QualityIndicator<T> qi) {
		return p.isFirstBetter(x, y, qi);
	}
	
	/**
	 * Use only on multiobjective problems!
	 * @param ind <code>MOIndividual</code> to be evaluated
	 * @throws StopCriterionException
	 */
	public void eval(MOSolutionBase<T> ind) throws StopCriterionException {
		
		if (stopCriterion == EnumStopCriterion.EVALUATIONS) {
			incEvaluate();
			p.evaluate(ind);
			p.evaluateConstraints(ind);
			GraphDataRecorder.AddRecord(ind, this.getProblemName());
		}
		else if(stopCriterion == EnumStopCriterion.ITERATIONS)
		{
			if(isStop)
				throw new StopCriterionException("Max iterations");
			incEvaluate();
			p.evaluate(ind);
			p.evaluateConstraints(ind);
			GraphDataRecorder.AddRecord(ind, this.getProblemName());
		}
		else if(stopCriterion == EnumStopCriterion.CPU_TIME)
		{
			if(!isStop)
			{
				hasTheCPUTimeBeenExceeded(); // if CPU time is exceed allow last eval
				incEvaluate();
				p.evaluate(ind);
				p.evaluateConstraints(ind);
				GraphDataRecorder.AddRecord(ind, this.getProblemName());
			}
			else
			{
				throw new StopCriterionException("CPU Time");
			}
		}
	}

	/**
	 * Returns a deep copy of the Task object 
	 * @return
	 */
	abstract public MOTask returnCopy();

    /**
     * Works only for basic interval setting!
     * Sets interval!
     * for example -40<x_i<40 <p>
     * if x_i <-40 -> -40 same for 40!
     * 
     * @param d value
     * @param i index of dimension
     * @return
     */
    /*public double feasible(double d, int i){
        return p.feasible(d, i);
    }*/
}
