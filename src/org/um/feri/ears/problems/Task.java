package org.um.feri.ears.problems;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.graphing.recording.GraphDataRecorder;


/**
* Task is main class, for communication between algorithm and problem  
* <p>
* 
* @author Matej Crepinsek
* @version 1
* 
*          <h3>License</h3>
* 
*          Copyright (c) 2011 by Matej Crepinsek. <br>
*          All rights reserved. <br>
* 
*          <p>
*          Redistribution and use in source and binary forms, with or without
*          modification, are permitted provided that the following conditions
*          are met:
*          <ul>
*          <li>Redistributions of source code must retain the above copyright
*          notice, this list of conditions and the following disclaimer.
*          <li>Redistributions in binary form must reproduce the above
*          copyright notice, this list of conditions and the following
*          disclaimer in the documentation and/or other materials provided with
*          the distribution.
*          <li>Neither the name of the copyright owners, their employers, nor
*          the names of its contributors may be used to endorse or promote
*          products derived from this software without specific prior written
*          permission.
*          </ul>
*          <p>
*          THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
*          "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
*          LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
*          FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
*          COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
*          INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
*          BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
*          LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
*          CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
*          LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
*          ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
*          POSSIBILITY OF SUCH DAMAGE.
* 
*/
public class Task extends TaskBase<Problem> {

	/**
	 * @param stop the stopping criteria
	 * @param eval the maximum number of evaluations allowed
	 * @param allowedTime the maximum CPU time allowed in milliseconds
	 * @param epsilon the epsilon value for global optimum
	 * @param p the problem
	 */
	

	
	public Task(EnumStopCriteria stop, int eval, long allowedTime, int maxIterations, double epsilon, Problem p) {
	    this(stop, eval, allowedTime, maxIterations, epsilon, p,  (int) Math.log10((1./epsilon)+1));
	}
	
    public Task(EnumStopCriteria stop, int eval, long allowedTime, int maxIterations, double epsilon, Problem p, int precisonOfRealNumbers) {
        precisionOfRealNumbersInDecimalPlaces = precisonOfRealNumbers;
        stopCriteria = stop;
        maxEvaluations = eval;
        numberOfEvaluations = 0;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
        isStop = false;
        isGlobal = false;
        this.p = p;
        this.allowedCPUTime = TimeUnit.MILLISECONDS.toNanos(allowedTime);
        
        // set initial best eval
        bestEval = p.isMinimum()? Double.MAX_VALUE : Double.MIN_VALUE;  
    }
    

	public double[] getInterval(){
		double interval[] = new double[p.upperLimit.size()];
		for (int i=0; i<interval.length;i++) {
			interval[i] = p.upperLimit.get(i) - p.lowerLimit.get(i);
		}
		return interval;
	}


	public DoubleSolution getRandomSolution() throws StopCriteriaException {

		if (stopCriteria == EnumStopCriteria.EVALUATIONS) {
			incEvaluate();
			long start = System.nanoTime();
			DoubleSolution tmpSolution = p.getRandomSolution();
			evaluationTime +=  System.nanoTime() - start;
			checkIfGlobalReached(tmpSolution.getEval());
			GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
			if(isAncestorLogginEnabled)
			{
				ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";");
				ancestorSB.append("\n");
			}

			return tmpSolution;
		}
		else if(stopCriteria == EnumStopCriteria.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
			if (isGlobal)
				throw new StopCriteriaException("Global optimum already found");
			incEvaluate();
			long start = System.nanoTime();
			DoubleSolution tmpSolution = p.getRandomSolution();
			evaluationTime +=  System.nanoTime() - start;
			checkIfGlobalReached(tmpSolution.getEval());
			GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
			if(isAncestorLogginEnabled)
			{
				ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";");
				ancestorSB.append("\n");
			}
			return tmpSolution;
		}
		else if(stopCriteria == EnumStopCriteria.ITERATIONS)
		{
			if(isStop)
				throw new StopCriteriaException("Max iterations");
			
			incEvaluate();
			long start = System.nanoTime();
			DoubleSolution tmpSolution = p.getRandomSolution();
			evaluationTime +=  System.nanoTime() - start;
			checkIfGlobalReached(tmpSolution.getEval());
			GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
			if(isAncestorLogginEnabled)
			{
				ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";");
				ancestorSB.append("\n");
			}

			return tmpSolution;
		}
		else if(stopCriteria == EnumStopCriteria.CPU_TIME)
		{
			// check if the CPU time is not exceeded yet
			if(!isStop)
			{
				hasTheCPUTimeBeenExceeded(); // if CPU time is exceed allow last eval
				incEvaluate();
				long start = System.nanoTime();
				DoubleSolution tmpSolution = p.getRandomSolution();
				evaluationTime +=  System.nanoTime() - start;
				checkIfGlobalReached(tmpSolution.getEval());
				GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
				if(isAncestorLogginEnabled)
				{
					ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";");
					ancestorSB.append("\n");
				}

				return tmpSolution;
			}
			else
			{
				throw new StopCriteriaException("CPU Time");
			}
		}
		else if(stopCriteria == EnumStopCriteria.STAGNATION)
		{
			if(isStop)
				throw new StopCriteriaException("Solution stagnation");
			
			incEvaluate();
			long start = System.nanoTime();
			DoubleSolution tmpSolution = p.getRandomSolution();
			evaluationTime +=  System.nanoTime() - start;
			checkIfGlobalReached(tmpSolution.getEval());
			checkImprovment(tmpSolution.getEval());
			GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
			if(isAncestorLogginEnabled)
			{
				ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";");
				ancestorSB.append("\n");
			}

			return tmpSolution;
		}

		return null;
	}
	

	public boolean isFirstBetter(DoubleSolution x, DoubleSolution y) {
		return p.isFirstBetter(x.getVariables(), x.getEval(), y.getVariables(), y.getEval());
	}
	

	/**
	 * This function is not ok, because you do not get informations about
	 * constrains, etc.. Just value
	 *  
	 * @see org.um.feri.ears.problems.Task#eval(double[])
	 * @deprecated
	 * @param ds
	 * @return
	 * @throws StopCriteriaException
	 */
	/*public double justEval(double[] ds) throws StopCriteriaException {
        if (stopCriteria == EnumStopCriteria.EVALUATIONS) {
            incEvaluate();
            return p.eval(ds);
        }
        if (stopCriteria == EnumStopCriteria.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
            if (isGlobal)
                throw new StopCriteriaException("Global optimum already found");
            incEvaluate();
            double d = p.eval(ds);
            if (Math.abs(d - p.getOptimumEval()) <= epsilon) {
                isGlobal = true;
            }
            return d;
        }
        assert false; // Execution should never reach this point!
        return Double.MAX_VALUE; //error
    }*/
	

	 /**
     * with no evaluations just checks
     * if algorithm result is in interval.
     * This is not checking constrains, just basic intervals!  
     * Delegated from Problem!
     * 
     * @param ds vector of possible solution
     * @return
     */
	public boolean areDimensionsInFeasableInterval(List<Double> ds) {
	    return p.areDimensionsInFeasableInterval(ds);
	}

	/**
	 * Better use method eval returns Individual with calculated fitness and constrains
	 * @deprecated
	 * 
	 * @param ds real vector to be evaluated (just calc constraints)
	 * @return
	 */
	public double[] calcConstrains(List<Double> ds) {
	    return p.calc_constrains(ds);
	}

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
    public double setFeasible(double d, int i){
        return p.setFeasible(d, i);
    }
    
    public double[] setFeasible(double[] d){
        return p.setFeasible(d);
    }
    
    public void setFeasible(DoubleSolution sol)
    {
    	sol.variable = p.setFeasible(sol.variable);
    }
    
	public boolean isFeasible(double x, int d) {
		
		return p.isFeasble(x, d);
	}
    

    /**
     * @deprecated
     * @param d
     * @param bestEvalCond
     * @return
     */
    public boolean isFirstBetter(double a, double b) {
        return p.isFirstBetter(a, b);
    }


    public double[] getLowerLimit() {

    	double[] target = new double[p.lowerLimit.size()];
    	for (int i = 0; i < target.length; i++) {
    		target[i] = p.lowerLimit.get(i); 
    	}
    	return  target;
    }

	public double[] getUpperLimit() {
    	double[] target = new double[p.upperLimit.size()];
    	for (int i = 0; i < target.length; i++) {
    		target[i] = p.upperLimit.get(i); 
    	}
    	return  target;
	}

	
	public DoubleSolution eval(double[] x) throws StopCriteriaException {
		
		Double[] inputBoxed = ArrayUtils.toObject(x);
		List<Double> ds = Arrays.asList(inputBoxed);
		
		if (stopCriteria == EnumStopCriteria.EVALUATIONS) {
			incEvaluate();
			long start = System.nanoTime();
			DoubleSolution tmpSolution = new DoubleSolution(ds,p.eval(ds),p.calc_constrains(ds),p.upperLimit,p.lowerLimit);
			checkIfGlobalReached(tmpSolution.getEval());
			GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
			return tmpSolution;
		}
		else if(stopCriteria == EnumStopCriteria.ITERATIONS)
		{
			if(isStop)
				throw new StopCriteriaException("Max iterations");
			incEvaluate();
			long start = System.nanoTime();
			DoubleSolution tmpSolution = new DoubleSolution(ds,p.eval(ds),p.calc_constrains(ds),p.upperLimit,p.lowerLimit);
			evaluationTime +=  System.nanoTime() - start;
			checkIfGlobalReached(tmpSolution.getEval());
			GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
			return tmpSolution;
		}
		else if(stopCriteria == EnumStopCriteria.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
			if (isGlobal)
				throw new StopCriteriaException("Global optimum already found");
			incEvaluate();
			long start = System.nanoTime();
			double d = p.eval(ds);
			evaluationTime +=  System.nanoTime() - start;
			checkIfGlobalReached(d);
			DoubleSolution tmpSolution = new DoubleSolution(ds,d,p.calc_constrains(ds),p.upperLimit,p.lowerLimit);
			GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
			return tmpSolution;
		}
		else if(stopCriteria == EnumStopCriteria.CPU_TIME)
		{
			if(!isStop)
			{
				hasTheCPUTimeBeenExceeded(); // if CPU time is exceed allow last eval
				incEvaluate();
				long start = System.nanoTime();
				DoubleSolution tmpSolution = new DoubleSolution(ds,p.eval(ds),p.calc_constrains(ds),p.upperLimit,p.lowerLimit);
				evaluationTime +=  System.nanoTime() - start;
				checkIfGlobalReached(tmpSolution.getEval());
				GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
				return tmpSolution;
			}
			else
			{
				throw new StopCriteriaException("CPU Time");
			}
		}
		else if(stopCriteria == EnumStopCriteria.STAGNATION)
		{
			if(isStop)
				throw new StopCriteriaException("Solution stagnation");
			
			incEvaluate();
			long start = System.nanoTime();
			DoubleSolution tmpSolution = new DoubleSolution(ds,p.eval(ds),p.calc_constrains(ds),p.upperLimit,p.lowerLimit);
			evaluationTime +=  System.nanoTime() - start;
			checkIfGlobalReached(tmpSolution.getEval());
			checkImprovment(tmpSolution.getEval());
			GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
			if(isAncestorLogginEnabled)
			{
				ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";");
				ancestorSB.append("\n");
			}

			return tmpSolution;
		}

		
		assert false; // Execution should never reach this point!
		return null; //error
	}

	private void checkIfGlobalReached(double d) {
		
		if (Math.abs(d - p.getOptimumEval()) <= epsilon) {
			isGlobal = true;
		}
		
	}

	public DoubleSolution eval(double[] x, List<DoubleSolution> parents) throws StopCriteriaException {
		
		DoubleSolution tmpSolution = eval(x);
		
		if(isAncestorLogginEnabled)
		{
			
			ancestorSB.append(tmpSolution.getID()+";"+tmpSolution.getEval()+";"+Arrays.toString(tmpSolution.getDoubleVariables())+";[");
			for(int i = 0; i < parents.size(); i++)
			{
				ancestorSB.append(parents.get(i).getID());
				if(i+1 < parents.size())
					ancestorSB.append(",");
			}

			ancestorSB.append("]\n");
		}
		
		return tmpSolution;
	}
	
	protected void checkImprovment(double eval)
	{
		if(p.isMinimum())
		{
			if(eval < bestEval)
			{
				bestEval = eval;
				stagnationTrials = 0;
			}
			else
			{
				stagnationTrials++;
			}
		}
		else
		{
			if(eval > bestEval)
			{
				bestEval = eval;
				stagnationTrials = 0;
			}
			else
			{
				stagnationTrials++;
			}
		}
		
		if(stagnationTrials >= maxEvaluationsBeforStagnation)
		{
			isStop = true;
		}
	}

	public static void resetLoggingID() {
			SolutionBase.resetLoggingID();	
	}

	public DoubleSolution eval(DoubleSolution newSolution) throws StopCriteriaException {
		return newSolution = eval(newSolution.getDoubleVariables());
	}
}
