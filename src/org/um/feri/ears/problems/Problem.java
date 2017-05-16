package org.um.feri.ears.problems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.util.Util;

/**
* Main common class for constrained and unconstrained problems.
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
public abstract class Problem extends ProblemBase<Double> {
	
	protected double[][] optimum;
	protected int numberOfGlobalOptima = 1;
	
	public Problem(int numberOfDimensions, int numberOfConstraints, int numberOfGlobalOptima) {
		super(numberOfDimensions, numberOfConstraints);
		
		this.numberOfGlobalOptima = numberOfGlobalOptima;
		
		optimum = new double[numberOfGlobalOptima][numberOfDimensions];
		Arrays.fill(optimum[0], 0); // default global optimum is [0, 0, ...., 0, 0]
	}
	
    public Problem(int numberOfDimensions, int numberOfConstraints) {
		this(numberOfDimensions, numberOfConstraints,1);
	}
	
	/**
	 * with no evaluations just checks
	 * if algorithm result is in interval.
	 * This is not checking constrains, just basic intervals!  
	 * 
	 * @param ds vector of possible solution
	 * @return
	 */
    public boolean areDimensionsInFeasableInterval(List<Double> ds) {
	    for (int i=0; i<numberOfDimensions; i++) {
        if (ds.get(i) < lowerLimit.get(i))
            return false;
        if (ds.get(i) > upperLimit.get(i))
            return false;
	    }
        return true;
	}
    
	/**
	 * If selected value in interval is not feasible  
	 * 
	 * @param d
	 * @param i
	 * @return
	 */
	public double setFeasible(double d, int i) {
		if (d < lowerLimit.get(i))
			return lowerLimit.get(i);
		if (d > upperLimit.get(i))
		return upperLimit.get(i);
		
		return d;
	}
	
	public double[] setFeasible(double[] d) {
		for(int i = 0; i < d.length;i++)
		{
			d[i] = setFeasible(d[i], i);
		}
		return d;
	}
	
	public List<Double> setFeasible(List<Double> d) {
		for(int i = 0; i < d.size();i++)
		{
			d.set(i,setFeasible(d.get(i), i));
		}
		return d;
	}
	
	public boolean isFeasble(double d, int i) {
		if (d < lowerLimit.get(i))
			return false;
		if (d > upperLimit.get(i))
		return false;
		
		return true;
	}
		
	@Override
	public String toString() {

		return "Problem: "+name+ " version: "+version+" dimensions: "+ numberOfDimensions+" constraints: "+numberOfConstraints;
	}

	public static final EnumProblemTypes TYPE = EnumProblemTypes.SORPO;
	
	/**
	 * It is 2 dimensional, because some problems can have more
	 * than one global optimum.
	 * 
	 * @return global optimum
	 */
	public double[][] getOptimalVector() {
		return optimum;
	}

	/**
	 * if there is more than one global optimum, you need to override this method.
	 * 
	 * @return number of global optimum-s
	 */
	public int getNumberOfGlobalOptima() {
		return numberOfGlobalOptima;
	}
	
	/**
	 * Calculates Euclidian Distance that is normalized by dimension interval.  
	 * 
	 * @param x_i
	 * @param x_j
	 * @return
	 */
	public double normalizedEuclidianDistance(double x_i[], double x_j[]) {
		double r = 0;
		for (int i = 0; i < numberOfDimensions; i++) {
			r += (x_i[i] - x_j[i]) / upperLimit.get(i) * (x_i[i] - x_j[i])
					/ upperLimit.get(i);
		}
		r = Math.sqrt(1. / numberOfDimensions * r);
		return r;
	}
	
	/**
	 * 
	 * @param tmp_constrains
	 */
	public void setMaxConstrains(double tmp_constrains[]) {
		for (int i = 0; i < numberOfConstraints; i++) {
			if (tmp_constrains[i] > max_constraints[i]) {
				max_constraints[i] = tmp_constrains[i];
			}
		}
	}

	public void setSumConstrains(double tmp_constrains[]) {
		for (int i = 0; i < numberOfConstraints; i++) {
			if (tmp_constrains[i] > 0) {
				sum_constraints[i] += tmp_constrains[i];
			}
		}
	}

	public void setMinConstrains(double tmp_constrains[]) {
		for (int i = 0; i < numberOfConstraints; i++) {
			if (tmp_constrains[i] > 0) {
				if ((tmp_constrains[i] < min_constraints[i])
						|| (min_constraints[i] == 0)) {
					min_constraints[i] = tmp_constrains[i];
				}
			}
		}
	}

	public void setCountConstrains(double tmp_constrains[]) {
		for (int i = 0; i < numberOfConstraints; i++) {
			if (tmp_constrains[i] > 0) {

				count_constraints[i]++;
			}

		}
	}

	public void countConstrains(double tmp_constrains[]) {
		// if (max_constrains==null) max_constrains = new double[constrains];
		for (int i = 0; i < numberOfConstraints; i++) {
			if (tmp_constrains[i] > max_constraints[i]) {
				max_constraints[i]++;
			} else {
				max_constraints[i]--;
				if (max_constraints[i] < 0) {
					max_constraints[i] = 0.0;
				}
			}

		}
	}

	public double[] normalizeConstrain(double tmp_constrains[]) {
		for (int i = 0; i < numberOfConstraints; i++) {

			tmp_constrains[i] = tmp_constrains[i] / max_constraints[i];
		}
		return tmp_constrains;
	}
	

	/**
	 * When overriding, call incEvaluate!
	 * Implement problem! 
	 * @param ds
	 * @return
	 */
	public abstract double eval(double[] ds);
	
	public double eval(List<Double> ds) {
		return eval(ds.stream().mapToDouble(i->i).toArray());
	}
	
	public double eval(Double[] ds) {
		return eval(ArrayUtils.toPrimitive(ds));
	}
	
	/**
	 * with no evaluations just checks
	 * if algorithm result is in interval.
	 * This is not checking constrains, just basic intervals!  
	 * 
	 * @param ds vector of possible solution
	 * @return
	 */
	/*public boolean areDimensionsInFeasableInterval(double[] ds) {
	    for (int i=0; i<numberOfDimensions; i++) {
        if (ds[i] < lowerLimit.get(i))
            return false;
        if (ds[i] > (lowerLimit.get(i) + upperLimit.get(i)))
            return false;
	    }
        return true;
	    
	}*/

	/**
	 * Important! Do not use this function for constrained problems,
	 * if fitness is not reflecting feasibility of the solution.
	 * 
	 * @param a first fitness
	 * @param b second fitness
	 * @return
	 */
	public boolean isFirstBetter(double a, double b) {
		if (minimum)
			return a < b;
		return a > b;
	}
	/**
	 * Default value
	 * @return
	 */
	public double getHitEpsilon() {
		return 0.1;  
	}

	public double getOptimumEval() {
		return 0;
	}
	/**
	 * Default it sets to zero!
	 * This method needs to be override in constrained based problems. 
	 * 
	 * @param x
	 * @return
	 */
	public double[] calc_constrains(List<Double> x) {
		double[] tmp = new double[0];
		return tmp;
	}

	public double[] calc_constrains(double[] x) {
		double[] tmp = new double[0];
		return tmp;
	}

	/**
	 * 
	 * 
	 * @param x - solution
	 * @return
	 */
	public double constrainsEvaluations(List<Double> x) {
		if (numberOfConstraints == 0)
			return 0;
		double[] g = calc_constrains(x); //calculate for every constrain (problem depended)
		double d = 0;
		for (int j = 0; j < numberOfConstraints; j++) {
			if (g[j] > 0) {
				if (constrained_type== CONSTRAINED_TYPE_COUNT) d++;
				if (constrained_type== CONSTRAINED_TYPE_SUM) d += g[j];
				if (constrained_type== CONSTRAINED_TYPE_NORMALIZATION)
				  d += g[j] * normalization_constraints_factor[j];// *(count_constrains[j]+1);
														
			}
		}
		return d;
	}
	/**
	 * Generates a random non evaluated solution.
	 * @return generated solution
	 */
	public DoubleSolution getRandomSolution()
	{
		//Double[] var=new Double[numberOfDimensions];
		ArrayList<Double> var = new ArrayList<Double>();
		for (int j = 0; j < numberOfDimensions; j++) {
			//var[j] = Util.nextDouble(lowerLimit.get(j), upperLimit.get(j));
			var.add(Util.nextDouble(lowerLimit.get(j), upperLimit.get(j)));
		}
		DoubleSolution sol = new DoubleSolution(var, eval(var), calc_constrains(var), upperLimit, lowerLimit);
		return sol;
	}
	
	/**
	 * Not just fitness value but also constrained. 
	 * 
	 * @param x
	 * @param eval_x
	 * @param y
	 * @param eval_y
	 * @return
	 */
	public boolean isFirstBetter(List<Double> x, double eval_x, List<Double> y,
			double eval_y) {
		double cons_x = constrainsEvaluations(x);
		double cons_y = constrainsEvaluations(y);
		if (cons_x == 0) {
			if (cons_y == 0) {
				if (minimum)
					return eval_x < eval_y;
				return eval_x > eval_y;
			}
			return true; // y is not feasible
		}
		if (cons_y == 0) {
			return false;
		}
		return cons_x < cons_y; // less constrain is better

	}

	public double[] getRandomVariables() {
		double[] var=new double[numberOfDimensions];
		for (int j = 0; j < numberOfDimensions; j++) {
			var[j] = Util.nextDouble(lowerLimit.get(j), upperLimit.get(j));
		}
		return var;
	}
}
