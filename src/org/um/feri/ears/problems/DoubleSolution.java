package org.um.feri.ears.problems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.util.Util;

/**
* In EA we often have population of Individuals (population based)!
* Individual has genotype (binary in our case vector of real values).
* Evaluation or fitness of genotype is eval value.
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
public class DoubleSolution extends SolutionBase<Double>{
    private double eval;
    public List<DoubleSolution> parents;
	
	public DoubleSolution(DoubleSolution s) {
		super(s);
		
		//upperLimit = new ArrayList<Double>(s.upperLimit);
		//lowerLimit = new ArrayList<Double>(s.lowerLimit);
	    
		eval = s.eval;
		parents = new ArrayList<DoubleSolution>();
	}
	
	public DoubleSolution(){}
	
	/**
	 * !!!This constructor is for unconstrained optimization!
	 * 
	 * @param x
	 * @param eval
	 * @deprecated
	 */
	public DoubleSolution(Double[] x, double eval) {
		
		variable = Arrays.copyOf(x,x.length);
		this.eval = eval;
		feasible = true;
	}
	/**
	 * Use this constructor only in case of constrained optimization 
	 * 
	 * @param x
	 * @param eval
	 * @param constrains
	 * @param upperLimit 
	 * @param lowerLimit 
	 */
	public DoubleSolution(Double[] x, double eval, double[] constrains, List<Double> upperLimit, List<Double> lowerLimit) {
	       	
			variable = Arrays.copyOf(x,x.length);
			//System.arraycopy(x, 0, this.variable, 0, x.length);
			
			upperLimit = new ArrayList<Double>(upperLimit);
			lowerLimit = new ArrayList<Double>(lowerLimit);
	        setFeasible(constrains);
	        this.eval = eval;
	}

    public DoubleSolution(double[] ds, double eval2, double[] calc_constrains, List<Double> upperLimit, List<Double> lowerLimit) {
		this(ArrayUtils.toObject(ds), eval2, calc_constrains, upperLimit, lowerLimit);
	}

	private void setFeasible(double[] constrains) {
        feasible = true;
	    for (int i=0;i<constrains.length; i++) {
	        if (constrains[i]>0) { //equal constrained needs to be solve in Problem (set 0 if<=0.001)
	            feasible = false;
	            this.constraints= new double[constrains.length];
	            System.arraycopy(constrains, 0, this.constraints, 0, constrains.length);
	        }
	    }
	}

    /**
     * Outboxes the array of variables to a primitive array. 
     * @return the variables in an array of primitives.
     */
	public double[] getDoubleVariables() {
		return ArrayUtils.toPrimitive(variable);
	}

	public double getEval() {
		return eval;
	}
	
	public String toString() {
		return Util.dfcshort.format(eval)+" ["+Util.arrayToString(getDoubleVariables())+"]";
	}
}
