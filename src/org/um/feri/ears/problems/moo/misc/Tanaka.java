//  Tanaka.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
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

import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.functions.Tanaka_F1;
import org.um.feri.ears.problems.moo.functions.Tanaka_F2;

public class Tanaka extends DoubleMOProblem{
	
	public Tanaka() {
	     
		super(2,2,2);

		file_name = "Tanaka";
		name = "Tanaka";

	    upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);

		for (int var = 0; var < numberOfDimensions; var++){
			lowerLimit.add(10e-5);
			upperLimit.add(Math.PI);
		}

		this.addObjective(new Tanaka_F1());
		this.addObjective(new Tanaka_F2());
	}

	@Override
	public void evaluate(MOSolutionBase<Double> solution) {
		
		double[] x = new double[numberOfDimensions];
		for (int i = 0; i < numberOfDimensions; i++)
			x[i] = solution.getVariables().get(i);

		double obj[] = new double[functions.size()];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = functions.get(i).eval(x);
		}
		solution.setObjectives(obj);
		
	}

	@Override
	public void evaluateConstraints(MOSolutionBase<Double> solution) {
		double[] constraints = new double[numberOfConstraints];
		
		double[] dv = new double[numberOfDimensions];
		for (int i = 0; i < numberOfDimensions; i++)
			dv[i] = solution.getVariables().get(i);
	    
		double x1 = dv[0];
	    double x2 = dv[1];
	        
	    constraints[0] = (x1*x1 + x2*x2 - 1.0 - 0.1*Math.cos(16.0*Math.atan(x1/x2)));
	    constraints[1] = - 2.0 * ( (x1-0.5)*(x1-0.5) + (x2-0.5)*(x2-0.5) - 0.5);

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
