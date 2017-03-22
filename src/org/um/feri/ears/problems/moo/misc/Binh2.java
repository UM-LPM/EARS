//  Binh2.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
//  Copyright (c) 2012 Antonio J. Nebro
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
package org.um.feri.ears.problems.moo.misc;

import java.util.ArrayList;

import javax.management.JMException;

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.functions.Binh2_F1;
import org.um.feri.ears.problems.moo.functions.Binh2_F2;

/**
 * The Binh (2) problem.
 * <p>
 * Properties:
 * <ul>
 *   <li>Connected Pareto set
 *   <li>Convex Pareto front
 *   <li>Constrained
 * </ul>
 * <p>
 * References:
 * <ol>
 *   <li>Binh, T. T. and Korn, U (1997).  "MOBES: A Multiobjective Evolution
 *       Strategy for Constrained Optimization Problems."  Proceedings of the
 *       Third International Conference on Genetic Algorithms (Mendel 97),
 *       pp. 176-182.
 *   <li>Van Veldhuizen, D. A (1999).  "Multiobjective Evolutionary Algorithms: 
 *       Classifications, Analyses, and New Innovations."  Air Force Institute
 *       of Technology, Ph.D. Thesis, Appendix B.
 * </ol>
 */
public class Binh2 extends DoubleMOProblem{
	

	public Binh2() {
     
		super(2,2,2);

		file_name = "Binh2";
		name = "Binh2";
	  
	    upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);

		lowerLimit.add(0.0);
		upperLimit.add(5.0);
		lowerLimit.add(0.0);
		upperLimit.add(3.0);
	    

		this.addObjective(new Binh2_F1());
		this.addObjective(new Binh2_F2());
	}
	    
	  /** 
	   * Evaluates a solution.
	   * @param solution The solution to evaluate.
	   * @throws JMException 
	   */
	public void evaluate(MOSolutionBase<Double> solution) {

		double[] x = ArrayUtils.toPrimitive(solution.getVariables());

		double obj[] = new double[functions.size()];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = functions.get(i).eval(x);
		}
		solution.setObjectives(obj);
	}
	
	public void evaluateConstraints(MOSolutionBase<Double> solution) {
		double[] constraints = new double[numberOfConstraints];
		
		double[] dv = ArrayUtils.toPrimitive(solution.getVariables());

		constraints[0] = -Math.pow(dv[0] - 5.0, 2.0) - Math.pow(dv[1], 2.0) + 25.0;
		constraints[1] = Math.pow(dv[0] - 8.0, 2.0) + Math.pow(dv[1] + 3.0, 2.0) - 7.7;

		solution.setConstraints(constraints);
		
	    double total = 0.0;
	    int number = 0;
		for (int i = 0; i < constraints.length; i++) {
			if (constraints[i]<0.0){
		        total+=constraints[i];
		        number++;
			}
		}
	    solution.setOverallConstraintViolation(total);    
	    solution.setNumberOfViolatedConstraint(number); 
	}

	@Override
	public double[] evaluate(Double[] ds) {

		double[] x = new double[numberOfDimensions];
		for (int i = 0; i < numberOfDimensions; i++)
			x[i] = ds[i];

		double obj[] = new double[functions.size()];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = functions.get(i).eval(x);
		}

		return obj;
	}

}
