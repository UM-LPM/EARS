//  ConstrEx.java
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

import org.apache.commons.lang3.ArrayUtils;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.MOSolutionBase;
import org.um.feri.ears.problems.moo.functions.ConstrEx_F1;
import org.um.feri.ears.problems.moo.functions.ConstrEx_F2;

public class ConstrEx extends DoubleMOProblem{

	public ConstrEx() {
	     
		super(2,2,2);

		file_name = "ConstrEx";
		name = "Constr_Ex";
	    
	    upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);

		lowerLimit.add(0.1);
		upperLimit.add(1.0);
		lowerLimit.add(0.0);
		upperLimit.add(5.0);

		this.addObjective(new ConstrEx_F1());
		this.addObjective(new ConstrEx_F2());
	}

	@Override
	public void evaluate(MOSolutionBase<Double> solution) {
		
		double[] x = solution.getVariables().stream().mapToDouble(i->i).toArray();


		double obj[] = new double[functions.size()];
		for (int i = 0; i < obj.length; i++) {
			obj[i] = functions.get(i).eval(x);
		}
		solution.setObjectives(obj);
		
	}

	@Override
	public void evaluateConstraints(MOSolutionBase<Double> solution) {
		double[] constraints = new double[numberOfConstraints];
		
		double[] dv = solution.getVariables().stream().mapToDouble(i->i).toArray();
		
	    constraints[0] =  (dv[1] + 9*dv[0]-6.0) ;
	    constraints[1] =  (-dv[1] + 9*dv[0] -1.0);

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
