package org.um.feri.ears.problems;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.graphing.recording.GraphDataRecorder;
import org.um.feri.ears.qualityIndicator.QualityIndicator;

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

	protected QualityIndicator qi; //is required for multiobjective optimization

	public Task(EnumStopCriteria stop, int eval, double epsilon, Problem p) {
	    this(stop, eval, epsilon, p,  (int) Math.log10((1./epsilon)+1));
	}
	
    public Task(EnumStopCriteria stop, int eval, double epsilon, Problem p, int precisonOfRealNumbers) {
        precisionOfRealNumbersInDecimalPlaces = precisonOfRealNumbers;
        stopCriteria = stop;
        maxEvaluations = eval;
        numberOfEvaluations = 0;
        this.epsilon = epsilon;
        isStop = false;
        isGlobal = false;
        this.p = p;
    }
    
	//TODO ?
	public double[] getIntervalRight(){
		double intervalR[] = new double[p.upperLimit.size()];
		for (int i=0; i<intervalR.length;i++) {
			intervalR[i] = p.lowerLimit.get(i)+p.upperLimit.get(i);
		}
		return intervalR;
	}


	public DoubleSolution getRandomIndividual() throws StopCriteriaException {

		if (stopCriteria == EnumStopCriteria.EVALUATIONS) {
			incEvaluate();
			DoubleSolution tmpSolution = p.getRandomSolution();
			GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
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

	public String getStopCriteriaDescription() {
        if (stopCriteria == EnumStopCriteria.EVALUATIONS) {
            return "E="+getMaxEvaluations();
        }
        if (stopCriteria == EnumStopCriteria.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
                return "Global optimum epsilon="+epsilon+" or  E="+getMaxEvaluations();
        }
        return "not defined";
	}
	/**
	 * Better use method eval returns Individual with calculated fitness and constrains
	 * @deprecated
	 * 
	 * @param ds real vector to be evaluated (just calc constraines
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
    public double feasible(double d, int i){
        return p.feasible(d, i);
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


    @Override
    public String toString() {
        return "Task [stopCriteria=" + stopCriteria + ", maxEvaluations=" + maxEvaluations + ", numberOfEvaluations=" + numberOfEvaluations + ", epsilon="
                + epsilon + ", isStop=" + isStop + ", isGlobal=" + isGlobal + ", precisionOfRealNumbersInDecimalPlaces="
                + precisionOfRealNumbersInDecimalPlaces + ", p=" + p + "]";
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
			
			DoubleSolution tmpSolution = new DoubleSolution(ds,p.eval(ds),p.calc_constrains(ds),p.upperLimit,p.lowerLimit);
			GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
			return tmpSolution;
		}
		if (stopCriteria == EnumStopCriteria.GLOBAL_OPTIMUM_OR_EVALUATIONS) {
			if (isGlobal)
				throw new StopCriteriaException("Global optimum already found");
			incEvaluate();
			double d = p.eval(ds);
			if (Math.abs(d - p.getOptimumEval()) <= epsilon) {
				isGlobal = true;
			}
			DoubleSolution tmpSolution = new DoubleSolution(ds,d,p.calc_constrains(ds),p.upperLimit,p.lowerLimit);
			GraphDataRecorder.AddRecord(tmpSolution, this.getProblemName());
			return tmpSolution;
		}
		assert false; // Execution should never reach this point!
		return null; //error
	}
    
}
