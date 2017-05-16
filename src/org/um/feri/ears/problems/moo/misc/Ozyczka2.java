//  Osyczka2.java
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
import org.um.feri.ears.problems.moo.functions.Osyczka2_F1;
import org.um.feri.ears.problems.moo.functions.Osyczka2_F2;

public class Ozyczka2 extends DoubleMOProblem{
	
	public Ozyczka2() {
	     
		super(6,6,2);

		file_name = "Osyczka2";
		name = "Osyczka2";
		
	    upperLimit = new ArrayList<Double>(numberOfDimensions);
		lowerLimit = new ArrayList<Double>(numberOfDimensions);

		lowerLimit.add(0.0);
		lowerLimit.add(0.0);
		lowerLimit.add(1.0);
		lowerLimit.add(0.0);
		lowerLimit.add(1.0);
		lowerLimit.add(0.0);
		
		upperLimit.add(10.0);
		upperLimit.add(10.0);
		upperLimit.add(5.0);
		upperLimit.add(6.0);
		upperLimit.add(5.0);
		upperLimit.add(10.0);

		this.addObjective(new Osyczka2_F1());
		this.addObjective(new Osyczka2_F2());
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
		
		double x1,x2,x3,x4,x5,x6;
	    x1 = dv[0];
	    x2 = dv[1];
	    x3 = dv[2];
	    x4 = dv[3];
	    x5 = dv[4];
	    x6 = dv[5];
	        
	    constraints[0] = (x1 + x2)/2.0 - 1.0;
	    constraints[1] = (6.0 - x1 - x2)/6.0;
	    constraints[2] = (2.0 - x2 + x1)/2.0;
	    constraints[3] = (2.0 - x1 + 3.0*x2)/2.0;
	    constraints[4] = (4.0 - (x3-3.0)*(x3-3.0) - x4)/4.0;
	    constraints[5] = ((x5-3.0)*(x5-3.0) +x6 - 4.0)/4.0;

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
